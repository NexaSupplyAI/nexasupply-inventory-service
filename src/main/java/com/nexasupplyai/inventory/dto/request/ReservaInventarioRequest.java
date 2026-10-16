package com.nexasupplyai.inventory.dto.request;

import com.nexasupplyai.inventory.enums.EstadoReservaInventario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservaInventarioRequest(
        @NotNull UUID idAlmacen,
        @NotNull UUID idProducto,
        @NotNull UUID idOrdenExterna,
        @NotNull @DecimalMin(value = "0.000", inclusive = false)
        @Digits(integer = 11, fraction = 3) BigDecimal cantidad,
        EstadoReservaInventario estado,
        OffsetDateTime expiraEn
) {
}
