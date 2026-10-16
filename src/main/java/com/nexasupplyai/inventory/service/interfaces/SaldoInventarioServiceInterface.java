package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.SaldoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;

import java.util.List;
import java.util.UUID;

public interface SaldoInventarioServiceInterface {
    List<SaldoInventarioResponse> listarPorProducto(UUID idProducto);
    SaldoInventarioResponse obtenerPorId(UUID id);
    SaldoInventarioResponse crear(SaldoInventarioRequest request);
    SaldoInventarioResponse actualizar(UUID id, SaldoInventarioRequest request);
    void eliminar(UUID id);
}
