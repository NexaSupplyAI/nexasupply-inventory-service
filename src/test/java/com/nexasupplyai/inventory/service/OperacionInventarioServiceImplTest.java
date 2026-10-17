package com.nexasupplyai.inventory.service;

import com.nexasupplyai.inventory.dto.request.LineaReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.request.ReservaOrdenRequest;
import com.nexasupplyai.inventory.dto.request.EntradaInventarioRequest;
import com.nexasupplyai.inventory.enums.EstadoReservaInventario;
import com.nexasupplyai.inventory.entity.*;
import com.nexasupplyai.inventory.mapper.MovimientoInventarioMapper;
import com.nexasupplyai.inventory.mapper.ReservaInventarioMapper;
import com.nexasupplyai.inventory.repository.*;
import com.nexasupplyai.inventory.service.impl.OperacionInventarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperacionInventarioServiceImplTest {
    @Mock SaldoInventarioRepository saldos;
    @Mock ReservaInventarioRepository reservas;
    @Mock MovimientoInventarioRepository movimientos;
    @Mock AlmacenRepository almacenes;
    @Mock ProductoRepository productos;
    @Mock ReservaInventarioMapper reservaMapper;
    @Mock MovimientoInventarioMapper movimientoMapper;

    @Test
    void reservaStockDeFormaAtomica() {
        UUID orden = UUID.randomUUID(), almacenId = UUID.randomUUID(), productoId = UUID.randomUUID();
        Almacen almacen = new Almacen(); almacen.setId(almacenId); almacen.setActivo(true);
        Producto producto = new Producto(); producto.setId(productoId); producto.setActivo(true); producto.setSku("SKU-1");
        SaldoInventario saldo = new SaldoInventario(); saldo.setAlmacen(almacen); saldo.setProducto(producto);
        saldo.setCantidadEnMano(new BigDecimal("10")); saldo.setCantidadReservada(BigDecimal.ZERO);
        when(reservas.bloquearTodasPorOrden(orden)).thenReturn(List.of());
        when(saldos.bloquearPorAlmacenYProducto(almacenId, productoId)).thenReturn(Optional.of(saldo));
        when(reservas.save(any())).thenAnswer(i -> i.getArgument(0));
        when(movimientos.save(any())).thenAnswer(i -> i.getArgument(0));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        var respuesta = servicio.reservar(new ReservaOrdenRequest(orden,
                List.of(new LineaReservaInventarioRequest(almacenId, productoId, new BigDecimal("3"))), null));

        assertEquals(new BigDecimal("3"), saldo.getCantidadReservada());
        assertEquals("RESERVED", respuesta.resultado());
        assertFalse(respuesta.idempotente());
        verify(movimientos).save(any());
    }

    @Test
    void rechazaReservaSinStockDisponible() {
        UUID orden = UUID.randomUUID(), almacenId = UUID.randomUUID(), productoId = UUID.randomUUID();
        Almacen almacen = new Almacen(); almacen.setId(almacenId); almacen.setActivo(true);
        Producto producto = new Producto(); producto.setId(productoId); producto.setActivo(true); producto.setSku("SKU-1");
        SaldoInventario saldo = new SaldoInventario(); saldo.setAlmacen(almacen); saldo.setProducto(producto);
        saldo.setCantidadEnMano(new BigDecimal("2")); saldo.setCantidadReservada(BigDecimal.ZERO);
        when(reservas.bloquearTodasPorOrden(orden)).thenReturn(List.of());
        when(saldos.bloquearPorAlmacenYProducto(almacenId, productoId)).thenReturn(Optional.of(saldo));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        assertThrows(IllegalArgumentException.class, () -> servicio.reservar(new ReservaOrdenRequest(orden,
                List.of(new LineaReservaInventarioRequest(almacenId, productoId, new BigDecimal("3"))), null)));
        verifyNoInteractions(movimientos);
    }

    @Test
    void liberaReservaSinReducirStockEnMano() {
        UUID orden = UUID.randomUUID(), almacenId = UUID.randomUUID(), productoId = UUID.randomUUID();
        Almacen almacen = new Almacen(); almacen.setId(almacenId);
        Producto producto = new Producto(); producto.setId(productoId);
        SaldoInventario saldo = new SaldoInventario(); saldo.setAlmacen(almacen); saldo.setProducto(producto);
        saldo.setCantidadEnMano(new BigDecimal("10")); saldo.setCantidadReservada(new BigDecimal("3"));
        ReservaInventario reserva = new ReservaInventario(); reserva.setAlmacen(almacen); reserva.setProducto(producto);
        reserva.setIdOrdenExterna(orden); reserva.setCantidad(new BigDecimal("3"));
        reserva.setEstado(EstadoReservaInventario.ACTIVE);
        when(reservas.bloquearTodasPorOrden(orden)).thenReturn(List.of(reserva));
        when(saldos.bloquearPorAlmacenYProducto(almacenId, productoId)).thenReturn(Optional.of(saldo));
        when(movimientos.save(any())).thenAnswer(i -> i.getArgument(0));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        servicio.liberar(orden);

        assertEquals(new BigDecimal("10"), saldo.getCantidadEnMano());
        assertEquals(BigDecimal.ZERO, saldo.getCantidadReservada());
        assertEquals(EstadoReservaInventario.RELEASED, reserva.getEstado());
    }

    @Test
    void entradaIdempotenteNoIncrementaDosVeces() {
        UUID almacenId = UUID.randomUUID(), productoId = UUID.randomUUID(), referencia = UUID.randomUUID();
        MovimientoInventario existente = new MovimientoInventario();
        when(movimientos.findByClaveIdempotencia("receipt:1")).thenReturn(Optional.of(existente));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        servicio.registrarEntrada(new EntradaInventarioRequest(almacenId, productoId, BigDecimal.ONE,
                "GOODS_RECEIPT", referencia, "receipt:1", null, "tester"));

        verifyNoInteractions(saldos, almacenes, productos);
        verify(movimientos, never()).save(any());
    }

    @Test
    void consumirReservaReduceStockYReserva() {
        UUID orden = UUID.randomUUID(), almacenId = UUID.randomUUID(), productoId = UUID.randomUUID();
        Almacen almacen = new Almacen(); almacen.setId(almacenId);
        Producto producto = new Producto(); producto.setId(productoId);
        SaldoInventario saldo = new SaldoInventario(); saldo.setAlmacen(almacen); saldo.setProducto(producto);
        saldo.setCantidadEnMano(new BigDecimal("10")); saldo.setCantidadReservada(new BigDecimal("3"));
        ReservaInventario reserva = new ReservaInventario(); reserva.setAlmacen(almacen); reserva.setProducto(producto);
        reserva.setIdOrdenExterna(orden); reserva.setCantidad(new BigDecimal("3"));
        reserva.setEstado(EstadoReservaInventario.ACTIVE);
        when(reservas.bloquearTodasPorOrden(orden)).thenReturn(List.of(reserva));
        when(saldos.bloquearPorAlmacenYProducto(almacenId, productoId)).thenReturn(Optional.of(saldo));
        when(movimientos.save(any())).thenAnswer(i -> i.getArgument(0));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        servicio.consumir(orden);

        assertEquals(new BigDecimal("7"), saldo.getCantidadEnMano());
        assertEquals(BigDecimal.ZERO, saldo.getCantidadReservada());
        assertEquals(EstadoReservaInventario.CONFIRMED, reserva.getEstado());
    }
}
