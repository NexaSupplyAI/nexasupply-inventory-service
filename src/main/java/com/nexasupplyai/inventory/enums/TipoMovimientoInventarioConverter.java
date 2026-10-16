package com.nexasupplyai.inventory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoMovimientoInventarioConverter implements AttributeConverter<TipoMovimientoInventario, String> {
    @Override
    public String convertToDatabaseColumn(TipoMovimientoInventario atributo) {
        if (atributo == null) return null;
        return switch (atributo) {
            case ENTRADA -> "IN";
            case SALIDA -> "OUT";
            case AJUSTE_ENTRADA -> "ADJUSTMENT_IN";
            case AJUSTE_SALIDA -> "ADJUSTMENT_OUT";
            case TRANSFERENCIA_ENTRADA -> "TRANSFER_IN";
            case TRANSFERENCIA_SALIDA -> "TRANSFER_OUT";
            case RESERVA -> "RESERVATION";
            case LIBERACION -> "RELEASE";
        };
    }

    @Override
    public TipoMovimientoInventario convertToEntityAttribute(String valor) {
        if (valor == null) return null;
        return switch (valor) {
            case "IN" -> TipoMovimientoInventario.ENTRADA;
            case "OUT" -> TipoMovimientoInventario.SALIDA;
            case "ADJUSTMENT_IN" -> TipoMovimientoInventario.AJUSTE_ENTRADA;
            case "ADJUSTMENT_OUT" -> TipoMovimientoInventario.AJUSTE_SALIDA;
            case "TRANSFER_IN" -> TipoMovimientoInventario.TRANSFERENCIA_ENTRADA;
            case "TRANSFER_OUT" -> TipoMovimientoInventario.TRANSFERENCIA_SALIDA;
            case "RESERVATION" -> TipoMovimientoInventario.RESERVA;
            case "RELEASE" -> TipoMovimientoInventario.LIBERACION;
            default -> throw new IllegalArgumentException("Tipo de movimiento no válido: " + valor);
        };
    }
}
