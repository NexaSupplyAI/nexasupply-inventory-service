package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.AlmacenRequest;
import com.nexasupplyai.inventory.dto.response.AlmacenResponse;

import java.util.List;
import java.util.UUID;

public interface AlmacenServiceInterface {
    List<AlmacenResponse> listar(boolean soloActivos);
    AlmacenResponse obtenerPorId(UUID id);
    AlmacenResponse crear(AlmacenRequest request);
    AlmacenResponse actualizar(UUID id, AlmacenRequest request);
    void eliminar(UUID id);
}
