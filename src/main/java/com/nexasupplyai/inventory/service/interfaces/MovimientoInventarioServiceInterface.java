package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.MovimientoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;

import java.util.List;
import java.util.UUID;

public interface MovimientoInventarioServiceInterface {
    List<MovimientoInventarioResponse> listarPorProducto(UUID idProducto);
    MovimientoInventarioResponse obtenerPorId(UUID id);
    MovimientoInventarioResponse crear(MovimientoInventarioRequest request);
}
