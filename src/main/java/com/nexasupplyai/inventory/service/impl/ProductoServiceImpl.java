package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.ProductoRequest;
import com.nexasupplyai.inventory.dto.response.ProductoResponse;
import com.nexasupplyai.inventory.entity.Categoria;
import com.nexasupplyai.inventory.entity.Producto;
import com.nexasupplyai.inventory.exception.CategoriaNoEncontradaException;
import com.nexasupplyai.inventory.exception.ProductoNoEncontradoException;
import com.nexasupplyai.inventory.mapper.ProductoMapper;
import com.nexasupplyai.inventory.repository.CategoriaRepository;
import com.nexasupplyai.inventory.repository.ProductoRepository;
import com.nexasupplyai.inventory.service.interfaces.ProductoServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoServiceInterface {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    public List<ProductoResponse> listarPorCategoria(UUID idCategoria) {
        return productoRepository.findAllByCategoriaIdAndActivoTrue(idCategoria).stream()
                .map(productoMapper::aResponse).toList();
    }

    public ProductoResponse obtenerPorId(UUID id) {
        return productoMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        validarUnicidad(request, null);
        Producto producto = productoMapper.aEntidad(request, buscarCategoria(request.idCategoria()));
        return productoMapper.aResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(UUID id, ProductoRequest request) {
        Producto producto = buscarEntidad(id);
        validarUnicidad(request, id);
        productoMapper.actualizarEntidad(request, buscarCategoria(request.idCategoria()), producto);
        return productoMapper.aResponse(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(UUID id) {
        productoRepository.delete(buscarEntidad(id));
    }

    private Producto buscarEntidad(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    private Categoria buscarCategoria(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException(id));
    }

    private void validarUnicidad(ProductoRequest request, UUID idActual) {
        productoRepository.findBySku(request.sku()).filter(p -> !p.getId().equals(idActual))
                .ifPresent(p -> { throw new IllegalArgumentException("El SKU ya existe"); });
        if (request.codigoBarras() != null) {
            productoRepository.findByCodigoBarras(request.codigoBarras()).filter(p -> !p.getId().equals(idActual))
                    .ifPresent(p -> { throw new IllegalArgumentException("El código de barras ya existe"); });
        }
    }
}
