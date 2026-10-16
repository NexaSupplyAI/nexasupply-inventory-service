package com.nexasupplyai.inventory.exception;

import java.util.UUID;

public class AlmacenNoEncontradoException extends RuntimeException {
    public AlmacenNoEncontradoException(UUID id) {
        super("Almacén no encontrado: " + id);
    }
}
