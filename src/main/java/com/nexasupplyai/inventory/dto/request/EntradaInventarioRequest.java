package com.nexasupplyai.inventory.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record EntradaInventarioRequest(
        @NotNull UUID idAlmacen,
        @NotNull UUID idProducto,
        @NotNull @DecimalMin(value = "0.000", inclusive = false)
        @Digits(integer = 11, fraction = 3) BigDecimal cantidad,
        @NotBlank @Size(max = 40) String tipoReferencia,
        @NotNull UUID idReferencia,
        @NotBlank @Size(max = 120) String claveIdempotencia,
        @Size(max = 300) String motivo,
        @Size(max = 120) String idActor
) {
}
