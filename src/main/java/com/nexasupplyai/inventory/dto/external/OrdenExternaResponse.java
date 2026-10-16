package com.nexasupplyai.inventory.dto.external;

import java.util.UUID;

/**
 * Contrato mínimo consumido desde Order Service.
 * Debe mantenerse alineado con su endpoint de consulta de órdenes.
 */
public record OrdenExternaResponse(
        UUID id,
        String numeroOrden,
        String estado
) {
}
