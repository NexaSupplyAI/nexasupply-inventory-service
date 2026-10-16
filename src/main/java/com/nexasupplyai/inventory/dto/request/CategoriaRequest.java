package com.nexasupplyai.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoriaRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 140) String slug,
        String descripcion,
        UUID idCategoriaPadre,
        Boolean activo
) {
}
