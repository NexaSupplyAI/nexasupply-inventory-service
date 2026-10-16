package com.nexasupplyai.inventory.exception;

import java.util.UUID;

public class SaldoInventarioNoEncontradoException extends RuntimeException {
    public SaldoInventarioNoEncontradoException(UUID id) {
        super("Saldo de inventario no encontrado: " + id);
    }
}
