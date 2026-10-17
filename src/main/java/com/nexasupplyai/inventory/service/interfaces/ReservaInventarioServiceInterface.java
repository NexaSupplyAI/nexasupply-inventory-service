package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;

import java.util.List;
import java.util.UUID;

public interface ReservaInventarioServiceInterface {
    List<ReservaInventarioResponse> listarPorOrden(UUID idOrdenExterna);
    ReservaInventarioResponse obtenerPorId(UUID id);
}
