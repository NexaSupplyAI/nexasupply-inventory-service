package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.MovimientoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.entity.MovimientoInventario;
import com.nexasupplyai.inventory.entity.Producto;
import com.nexasupplyai.inventory.exception.AlmacenNoEncontradoException;
import com.nexasupplyai.inventory.exception.MovimientoInventarioNoEncontradoException;
import com.nexasupplyai.inventory.exception.ProductoNoEncontradoException;
import com.nexasupplyai.inventory.mapper.MovimientoInventarioMapper;
import com.nexasupplyai.inventory.repository.AlmacenRepository;
import com.nexasupplyai.inventory.repository.MovimientoInventarioRepository;
import com.nexasupplyai.inventory.repository.ProductoRepository;
import com.nexasupplyai.inventory.service.interfaces.MovimientoInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovimientoInventarioServiceImpl implements MovimientoInventarioServiceInterface {
    private final MovimientoInventarioRepository movimientoRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioMapper movimientoMapper;

    public List<MovimientoInventarioResponse> listarPorProducto(UUID idProducto) {
        return movimientoRepository.findAllByProductoIdOrderByOcurrioEnDesc(idProducto).stream()
                .map(movimientoMapper::aResponse).toList();
    }

    public MovimientoInventarioResponse obtenerPorId(UUID id) {
        return movimientoMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public MovimientoInventarioResponse crear(MovimientoInventarioRequest request) {
        if (request.claveIdempotencia() != null) {
            MovimientoInventario existente = movimientoRepository.findByClaveIdempotencia(request.claveIdempotencia())
                    .orElse(null);
            if (existente != null) return movimientoMapper.aResponse(existente);
        }
        MovimientoInventario movimiento = movimientoMapper.aEntidad(
                request, buscarAlmacen(request.idAlmacen()), buscarProducto(request.idProducto())
        );
        return movimientoMapper.aResponse(movimientoRepository.save(movimiento));
    }

    private MovimientoInventario buscarEntidad(UUID id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new MovimientoInventarioNoEncontradoException(id));
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
