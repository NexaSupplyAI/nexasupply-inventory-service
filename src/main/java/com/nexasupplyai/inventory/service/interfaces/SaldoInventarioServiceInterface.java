package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;

import java.util.List;
import java.util.UUID;

public interface SaldoInventarioServiceInterface {
    List<SaldoInventarioResponse> listar();
    List<SaldoInventarioResponse> listarPorProducto(UUID idProducto);
    List<SaldoInventarioResponse> listarStockBajo();
    SaldoInventarioResponse obtenerPorId(UUID id);
}
