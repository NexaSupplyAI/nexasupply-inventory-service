package com.nexasupplyai.inventory.dto.response;

import com.nexasupplyai.inventory.enums.EstadoReservaInventario;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservaInventarioResponse(
        UUID id,
        UUID idAlmacen,
        UUID idProducto,
        UUID idOrdenExterna,
        BigDecimal cantidad,
        EstadoReservaInventario estado,
        OffsetDateTime expiraEn,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn
) {
}
