package com.nexasupplyai.inventory.exception;

import java.util.UUID;

public class MovimientoInventarioNoEncontradoException extends RuntimeException {
    public MovimientoInventarioNoEncontradoException(UUID id) {
        super("Movimiento de inventario no encontrado: " + id);
    }
}
