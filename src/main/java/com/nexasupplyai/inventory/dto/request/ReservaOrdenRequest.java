package com.nexasupplyai.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ReservaOrdenRequest(
        @NotNull UUID idOrden,
        @NotEmpty List<@Valid LineaReservaInventarioRequest> lineas,
        OffsetDateTime expiraEn
) {
}
