package com.nexasupplyai.inventory.dto.request;

import com.nexasupplyai.inventory.enums.UnidadMedida;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record ProductoRequest(
        @NotBlank @Size(max = 80) String sku,
        @Size(max = 80) String codigoBarras,
        @NotBlank @Size(max = 180) String nombre,
        String descripcion,
        @NotNull UUID idCategoria,
        @NotNull UnidadMedida unidadMedida,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal precioVenta,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal stockMinimo,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal puntoReorden,
        @NotNull @DecimalMin("0.000") @Digits(integer = 11, fraction = 3) BigDecimal cantidadReorden,
        Boolean activo,
        Map<String, Object> atributos
) {
}
