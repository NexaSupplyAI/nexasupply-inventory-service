package com.nexasupplyai.inventory.dto.request;

import com.nexasupplyai.inventory.enums.TipoMovimientoInventario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record MovimientoInventarioRequest(
        @NotNull UUID idAlmacen,
        @NotNull UUID idProducto,
        @NotNull TipoMovimientoInventario tipoMovimiento,
        @NotNull @DecimalMin(value = "0.000", inclusive = false)
        @Digits(integer = 11, fraction = 3) BigDecimal cantidad,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal saldoAnterior,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal saldoPosterior,
        @Size(max = 40) String tipoReferencia,
        UUID idReferencia,
        @Size(max = 120) String claveIdempotencia,
        @Size(max = 300) String motivo,
        @Size(max = 120) String idActor,
        Map<String, Object> metadatos,
        OffsetDateTime ocurrioEn
) {
}
