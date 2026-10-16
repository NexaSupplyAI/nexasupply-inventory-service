package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.CategoriaRequest;
import com.nexasupplyai.inventory.dto.response.CategoriaResponse;

import java.util.List;
import java.util.UUID;

public interface CategoriaServiceInterface {
    List<CategoriaResponse> listar(boolean soloActivas);
    CategoriaResponse obtenerPorId(UUID id);
    CategoriaResponse crear(CategoriaRequest request);
    CategoriaResponse actualizar(UUID id, CategoriaRequest request);
    void eliminar(UUID id);
}
