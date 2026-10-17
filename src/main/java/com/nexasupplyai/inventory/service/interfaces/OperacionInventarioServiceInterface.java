package com.nexasupplyai.inventory.service.interfaces;

import com.nexasupplyai.inventory.dto.request.EntradaInventarioRequest;
import com.nexasupplyai.inventory.dto.request.ReservaOrdenRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.dto.response.OperacionInventarioResponse;

import java.util.UUID;

public interface OperacionInventarioServiceInterface {
    OperacionInventarioResponse reservar(ReservaOrdenRequest request);
    OperacionInventarioResponse consumir(UUID idOrden);
    OperacionInventarioResponse liberar(UUID idOrden);
    MovimientoInventarioResponse registrarEntrada(EntradaInventarioRequest request);
}
