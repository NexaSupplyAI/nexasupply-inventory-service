package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.SaldoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.entity.Producto;
import com.nexasupplyai.inventory.entity.SaldoInventario;
import com.nexasupplyai.inventory.exception.AlmacenNoEncontradoException;
import com.nexasupplyai.inventory.exception.ProductoNoEncontradoException;
import com.nexasupplyai.inventory.exception.SaldoInventarioNoEncontradoException;
import com.nexasupplyai.inventory.mapper.SaldoInventarioMapper;
import com.nexasupplyai.inventory.repository.AlmacenRepository;
import com.nexasupplyai.inventory.repository.ProductoRepository;
import com.nexasupplyai.inventory.repository.SaldoInventarioRepository;
import com.nexasupplyai.inventory.service.interfaces.SaldoInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaldoInventarioServiceImpl implements SaldoInventarioServiceInterface {
    private final SaldoInventarioRepository saldoRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final SaldoInventarioMapper saldoMapper;

    public List<SaldoInventarioResponse> listarPorProducto(UUID idProducto) {
        return saldoRepository.findAllByProductoId(idProducto).stream().map(saldoMapper::aResponse).toList();
    }

    public List<SaldoInventarioResponse> listarStockBajo() {
        return saldoRepository.findAllConStockBajo().stream().map(saldoMapper::aResponse).toList();
    }

    public SaldoInventarioResponse obtenerPorId(UUID id) {
        return saldoMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public SaldoInventarioResponse crear(SaldoInventarioRequest request) {
        validarCantidades(request);
        Almacen almacen = buscarAlmacen(request.idAlmacen());
        Producto producto = buscarProducto(request.idProducto());
        saldoRepository.findByAlmacenIdAndProductoId(almacen.getId(), producto.getId())
                .ifPresent(s -> { throw new IllegalArgumentException("Ya existe un saldo para ese almacén y producto"); });
        return saldoMapper.aResponse(saldoRepository.save(saldoMapper.aEntidad(request, almacen, producto)));
    }

    @Transactional
    public SaldoInventarioResponse actualizar(UUID id, SaldoInventarioRequest request) {
        validarCantidades(request);
        SaldoInventario saldo = buscarEntidad(id);
        Almacen almacen = buscarAlmacen(request.idAlmacen());
        Producto producto = buscarProducto(request.idProducto());
        saldoRepository.findByAlmacenIdAndProductoId(almacen.getId(), producto.getId())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Ya existe un saldo para ese almacén y producto");
                });
        saldoMapper.actualizarEntidad(request, almacen, producto, saldo);
        return saldoMapper.aResponse(saldoRepository.save(saldo));
    }

    @Transactional
    public void eliminar(UUID id) {
        saldoRepository.delete(buscarEntidad(id));
    }

    private void validarCantidades(SaldoInventarioRequest request) {
        if (request.cantidadReservada().compareTo(request.cantidadEnMano()) > 0) {
            throw new IllegalArgumentException("La cantidad reservada no puede superar la cantidad en mano");
        }
    }

    private SaldoInventario buscarEntidad(UUID id) {
        return saldoRepository.findById(id)
                .orElseThrow(() -> new SaldoInventarioNoEncontradoException(id));
    }

    private Almacen buscarAlmacen(UUID id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new AlmacenNoEncontradoException(id));
    }

    private Producto buscarProducto(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }
}
