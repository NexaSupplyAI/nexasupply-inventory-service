package com.nexasupplyai.inventory.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoriaResponse(
        UUID id,
        String nombre,
        String slug,
        String descripcion,
        UUID idCategoriaPadre,
        Boolean activo,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn,
        Long version
) {
}
