package com.nexasupplyai.inventory.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AlmacenResponse(
        UUID id,
        String codigo,
        String nombre,
        String descripcion,
        String direccion,
        String distrito,
        String ciudad,
        String region,
        String codigoPais,
        Boolean activo,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn,
        Long version
) {
}
