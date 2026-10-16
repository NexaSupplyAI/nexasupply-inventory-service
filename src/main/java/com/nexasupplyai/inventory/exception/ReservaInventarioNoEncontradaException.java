package com.nexasupplyai.inventory.exception;

import java.util.UUID;

public class ReservaInventarioNoEncontradaException extends RuntimeException {
    public ReservaInventarioNoEncontradaException(UUID id) {
        super("Reserva de inventario no encontrada: " + id);
    }
}
