package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.ReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;

import java.util.List;
import java.util.UUID;

public interface ReservaInventarioServiceInterface {
    List<ReservaInventarioResponse> listarPorOrden(UUID idOrdenExterna);
    ReservaInventarioResponse obtenerPorId(UUID id);
    ReservaInventarioResponse crear(ReservaInventarioRequest request);
    ReservaInventarioResponse actualizar(UUID id, ReservaInventarioRequest request);
    void eliminar(UUID id);
}
