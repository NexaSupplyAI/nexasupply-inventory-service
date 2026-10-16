package com.nexasupplyai.inventory.exception;

import java.util.UUID;

public class CategoriaNoEncontradaException extends RuntimeException {
    public CategoriaNoEncontradaException(UUID id) {
        super("Categoría no encontrada: " + id);
    }
}
