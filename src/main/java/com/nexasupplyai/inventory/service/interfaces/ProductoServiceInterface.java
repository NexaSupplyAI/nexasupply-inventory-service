package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.ProductoRequest;
import com.nexasupplyai.inventory.dto.response.ProductoResponse;

import java.util.List;
import java.util.UUID;

public interface ProductoServiceInterface {
    List<ProductoResponse> listar();
    List<ProductoResponse> listarPorCategoria(UUID idCategoria);
    ProductoResponse obtenerPorId(UUID id);
    ProductoResponse crear(ProductoRequest request);
    ProductoResponse actualizar(UUID id, ProductoRequest request);
    void eliminar(UUID id);
}
