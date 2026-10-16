package com.nexasupplyai.inventory.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record SaldoInventarioRequest(
        @NotNull UUID idAlmacen,
        @NotNull UUID idProducto,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal cantidadEnMano,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal cantidadReservada
) {
}
