package com.nexasupplyai.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AlmacenRequest(
        @NotBlank @Size(max = 40) String codigo,
        @NotBlank @Size(max = 140) String nombre,
        String descripcion,
        @Size(max = 220) String direccion,
        @Size(max = 120) String distrito,
        @Size(max = 120) String ciudad,
        @Size(max = 120) String region,
        @NotBlank @Pattern(regexp = "^[A-Z]{2}$") String codigoPais,
        Boolean activo
) {
}
