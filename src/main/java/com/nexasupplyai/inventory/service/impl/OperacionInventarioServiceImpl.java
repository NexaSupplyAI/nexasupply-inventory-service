package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.EntradaInventarioRequest;
import com.nexasupplyai.inventory.dto.request.LineaReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.request.ReservaOrdenRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.dto.response.OperacionInventarioResponse;
import com.nexasupplyai.inventory.entity.*;
import com.nexasupplyai.inventory.enums.EstadoReservaInventario;
import com.nexasupplyai.inventory.enums.TipoMovimientoInventario;
import com.nexasupplyai.inventory.exception.AlmacenNoEncontradoException;
import com.nexasupplyai.inventory.exception.ProductoNoEncontradoException;
import com.nexasupplyai.inventory.mapper.MovimientoInventarioMapper;
import com.nexasupplyai.inventory.mapper.ReservaInventarioMapper;
import com.nexasupplyai.inventory.repository.*;
import com.nexasupplyai.inventory.service.interfaces.OperacionInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OperacionInventarioServiceImpl implements OperacionInventarioServiceInterface {
    private final SaldoInventarioRepository saldoRepository;
    private final ReservaInventarioRepository reservaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final ReservaInventarioMapper reservaMapper;
    private final MovimientoInventarioMapper movimientoMapper;

    @Override
    @Transactional
    public OperacionInventarioResponse reservar(ReservaOrdenRequest request) {
        validarLineasUnicas(request.lineas());
        List<ReservaInventario> existentes = reservaRepository.bloquearTodasPorOrden(request.idOrden());
        List<ReservaInventario> vigentes = existentes.stream()
                .filter(r -> r.getEstado() == EstadoReservaInventario.ACTIVE
                        || r.getEstado() == EstadoReservaInventario.CONFIRMED)
                .toList();
        if (!vigentes.isEmpty()) {
            validarMismaReserva(request, vigentes);
            return respuesta(request.idOrden(), "RESERVED", true, vigentes);
        }

        List<LineaReservaInventarioRequest> lineas = request.lineas().stream()
                .sorted(Comparator.comparing((LineaReservaInventarioRequest l) -> l.idProducto().toString())
                        .thenComparing(l -> l.idAlmacen().toString()))
                .toList();
        List<ReservaInventario> creadas = new ArrayList<>();
        for (LineaReservaInventarioRequest linea : lineas) {
            SaldoInventario saldo = saldoRepository.bloquearPorAlmacenYProducto(linea.idAlmacen(), linea.idProducto())
                    .orElseThrow(() -> new IllegalArgumentException("No existe saldo para el producto y almacén indicados"));
            validarActivos(saldo.getAlmacen(), saldo.getProducto());
            BigDecimal disponibleAntes = saldo.getCantidadEnMano().subtract(saldo.getCantidadReservada());
            if (disponibleAntes.compareTo(linea.cantidad()) < 0) {
                throw new IllegalArgumentException("Stock insuficiente para el producto " + saldo.getProducto().getSku());
            }
            saldo.setCantidadReservada(saldo.getCantidadReservada().add(linea.cantidad()));

            ReservaInventario reserva = new ReservaInventario();
            reserva.setAlmacen(saldo.getAlmacen());
            reserva.setProducto(saldo.getProducto());
            reserva.setIdOrdenExterna(request.idOrden());
            reserva.setCantidad(linea.cantidad());
            reserva.setEstado(EstadoReservaInventario.ACTIVE);
            reserva.setExpiraEn(request.expiraEn());
            creadas.add(reservaRepository.save(reserva));

            guardarMovimiento(saldo, TipoMovimientoInventario.RESERVATION, linea.cantidad(),
                    disponibleAntes, disponibleAntes.subtract(linea.cantidad()), "ORDER", request.idOrden(),
                    "reserve:" + request.idOrden() + ":" + linea.idAlmacen() + ":" + linea.idProducto(),
                    "Reserva de pedido");
        }
        return respuesta(request.idOrden(), "RESERVED", false, creadas);
    }

    @Override
    @Transactional
    public OperacionInventarioResponse consumir(UUID idOrden) {
        List<ReservaInventario> reservas = reservaRepository.bloquearTodasPorOrden(idOrden);
        if (reservas.isEmpty()) throw new IllegalArgumentException("La orden no tiene reservas de inventario");
        boolean idempotente = reservas.stream().allMatch(r -> r.getEstado() == EstadoReservaInventario.CONFIRMED);
        if (idempotente) return respuesta(idOrden, "CONSUMED", true, reservas);
        if (reservas.stream().anyMatch(r -> r.getEstado() != EstadoReservaInventario.ACTIVE)) {
            throw new IllegalStateException("La orden contiene reservas que no pueden consumirse");
        }

        for (ReservaInventario reserva : reservas) {
            SaldoInventario saldo = bloquearSaldo(reserva);
            BigDecimal anterior = saldo.getCantidadEnMano();
            if (saldo.getCantidadReservada().compareTo(reserva.getCantidad()) < 0
                    || saldo.getCantidadEnMano().compareTo(reserva.getCantidad()) < 0) {
                throw new IllegalStateException("El saldo no es consistente con la reserva de inventario");
            }
            saldo.setCantidadReservada(saldo.getCantidadReservada().subtract(reserva.getCantidad()));
            saldo.setCantidadEnMano(saldo.getCantidadEnMano().subtract(reserva.getCantidad()));
            reserva.setEstado(EstadoReservaInventario.CONFIRMED);
            guardarMovimiento(saldo, TipoMovimientoInventario.OUT, reserva.getCantidad(), anterior,
                    saldo.getCantidadEnMano(), "ORDER", idOrden,
                    "consume:" + idOrden + ":" + reserva.getAlmacen().getId() + ":" + reserva.getProducto().getId(),
                    "Salida por pedido completado");
        }
        return respuesta(idOrden, "CONSUMED", false, reservas);
    }

    @Override
    @Transactional
    public OperacionInventarioResponse liberar(UUID idOrden) {
        List<ReservaInventario> reservas = reservaRepository.bloquearTodasPorOrden(idOrden);
        if (reservas.isEmpty()) return respuesta(idOrden, "RELEASED", true, List.of());
        boolean idempotente = reservas.stream().allMatch(r -> r.getEstado() == EstadoReservaInventario.RELEASED);
        if (idempotente) return respuesta(idOrden, "RELEASED", true, reservas);
        if (reservas.stream().anyMatch(r -> r.getEstado() != EstadoReservaInventario.ACTIVE)) {
            throw new IllegalStateException("La orden contiene reservas que no pueden liberarse");
        }
        boolean cambio = false;
        for (ReservaInventario reserva : reservas) {
            SaldoInventario saldo = bloquearSaldo(reserva);
            if (saldo.getCantidadReservada().compareTo(reserva.getCantidad()) < 0) {
                throw new IllegalStateException("El saldo reservado es menor que la reserva a liberar");
            }
            BigDecimal disponibleAntes = saldo.getCantidadEnMano().subtract(saldo.getCantidadReservada());
            saldo.setCantidadReservada(saldo.getCantidadReservada().subtract(reserva.getCantidad()));
            reserva.setEstado(EstadoReservaInventario.RELEASED);
            guardarMovimiento(saldo, TipoMovimientoInventario.RELEASE, reserva.getCantidad(), disponibleAntes,
                    disponibleAntes.add(reserva.getCantidad()), "ORDER", idOrden,
                    "release:" + idOrden + ":" + reserva.getAlmacen().getId() + ":" + reserva.getProducto().getId(),
                    "Liberación de reserva de pedido");
            cambio = true;
        }
        return respuesta(idOrden, "RELEASED", !cambio, reservas);
    }

    @Override
    @Transactional
    public MovimientoInventarioResponse registrarEntrada(EntradaInventarioRequest request) {
        MovimientoInventario existente = movimientoRepository.findByClaveIdempotencia(request.claveIdempotencia()).orElse(null);
        if (existente != null) return movimientoMapper.aResponse(existente);

        Almacen almacen = almacenRepository.findById(request.idAlmacen())
                .orElseThrow(() -> new AlmacenNoEncontradoException(request.idAlmacen()));
        Producto producto = productoRepository.findById(request.idProducto())
                .orElseThrow(() -> new ProductoNoEncontradoException(request.idProducto()));
        validarActivos(almacen, producto);
        SaldoInventario saldo = saldoRepository.bloquearPorAlmacenYProducto(request.idAlmacen(), request.idProducto())
                .orElseGet(() -> nuevoSaldo(almacen, producto));
        BigDecimal anterior = saldo.getCantidadEnMano();
        saldo.setCantidadEnMano(anterior.add(request.cantidad()));
        MovimientoInventario movimiento = guardarMovimiento(saldo, TipoMovimientoInventario.IN, request.cantidad(),
                anterior, saldo.getCantidadEnMano(), request.tipoReferencia(), request.idReferencia(),
                request.claveIdempotencia(), request.motivo());
        movimiento.setIdActor(request.idActor());
        return movimientoMapper.aResponse(movimiento);
    }

    private SaldoInventario nuevoSaldo(Almacen almacen, Producto producto) {
        SaldoInventario saldo = new SaldoInventario();
        saldo.setAlmacen(almacen);
        saldo.setProducto(producto);
        saldo.setCantidadEnMano(BigDecimal.ZERO);
        saldo.setCantidadReservada(BigDecimal.ZERO);
        return saldoRepository.saveAndFlush(saldo);
    }

    private SaldoInventario bloquearSaldo(ReservaInventario reserva) {
        return saldoRepository.bloquearPorAlmacenYProducto(reserva.getAlmacen().getId(), reserva.getProducto().getId())
                .orElseThrow(() -> new IllegalStateException("La reserva no tiene saldo asociado"));
    }

    private MovimientoInventario guardarMovimiento(SaldoInventario saldo, TipoMovimientoInventario tipo,
                                                     BigDecimal cantidad, BigDecimal anterior, BigDecimal posterior,
                                                     String tipoReferencia, UUID idReferencia, String clave,
                                                     String motivo) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setAlmacen(saldo.getAlmacen());
        movimiento.setProducto(saldo.getProducto());
        movimiento.setTipoMovimiento(tipo);
        movimiento.setCantidad(cantidad);
        movimiento.setSaldoAnterior(anterior);
        movimiento.setSaldoPosterior(posterior);
        movimiento.setTipoReferencia(tipoReferencia);
        movimiento.setIdReferencia(idReferencia);
        movimiento.setClaveIdempotencia(clave);
        movimiento.setMotivo(motivo);
        movimiento.setMetadatos(Map.of());
        return movimientoRepository.save(movimiento);
    }

    private void validarActivos(Almacen almacen, Producto producto) {
        if (!Boolean.TRUE.equals(almacen.getActivo())) throw new IllegalArgumentException("El almacén está inactivo");
        if (!Boolean.TRUE.equals(producto.getActivo())) throw new IllegalArgumentException("El producto está inactivo");
    }

    private void validarMismaReserva(ReservaOrdenRequest request, List<ReservaInventario> existentes) {
        Map<String, BigDecimal> esperado = new HashMap<>();
        request.lineas().forEach(l -> esperado.put(l.idAlmacen() + ":" + l.idProducto(), l.cantidad()));
        Map<String, BigDecimal> actual = new HashMap<>();
        existentes.forEach(r -> actual.put(r.getAlmacen().getId() + ":" + r.getProducto().getId(), r.getCantidad()));
        if (!esperado.equals(actual)) throw new IllegalArgumentException("La orden ya tiene una reserva diferente");
    }

    private void validarLineasUnicas(List<LineaReservaInventarioRequest> lineas) {
        Set<String> claves = new HashSet<>();
        for (LineaReservaInventarioRequest linea : lineas) {
            if (!claves.add(linea.idAlmacen() + ":" + linea.idProducto())) {
                throw new IllegalArgumentException("La orden contiene líneas duplicadas para almacén y producto");
            }
        }
    }

    private OperacionInventarioResponse respuesta(UUID idOrden, String resultado, boolean idempotente,
                                                   List<ReservaInventario> reservas) {
        return new OperacionInventarioResponse(idOrden, resultado, idempotente,
                reservas.stream().map(reservaMapper::aResponse).toList());
    }
}
