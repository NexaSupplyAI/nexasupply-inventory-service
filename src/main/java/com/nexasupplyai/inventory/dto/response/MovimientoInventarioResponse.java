package com.nexasupplyai.inventory.dto.response;

import com.nexasupplyai.inventory.enums.TipoMovimientoInventario;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record MovimientoInventarioResponse(
        UUID id,
        UUID idAlmacen,
        UUID idProducto,
        TipoMovimientoInventario tipoMovimiento,
        BigDecimal cantidad,
        BigDecimal saldoAnterior,
        BigDecimal saldoPosterior,
        String tipoReferencia,
        UUID idReferencia,
        String claveIdempotencia,
        String motivo,
        String idActor,
        Map<String, Object> metadatos,
        OffsetDateTime ocurrioEn,
        OffsetDateTime creadoEn
) {
}
