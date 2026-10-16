package com.nexasupplyai.inventory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoReservaInventarioConverter implements AttributeConverter<EstadoReservaInventario, String> {
    @Override
    public String convertToDatabaseColumn(EstadoReservaInventario atributo) {
        if (atributo == null) return null;
        return switch (atributo) {
            case ACTIVA -> "ACTIVE";
            case CONFIRMADA -> "CONFIRMED";
            case LIBERADA -> "RELEASED";
            case CANCELADA -> "CANCELLED";
            case EXPIRADA -> "EXPIRED";
        };
    }

    @Override
    public EstadoReservaInventario convertToEntityAttribute(String valor) {
        if (valor == null) return null;
        return switch (valor) {
            case "ACTIVE" -> EstadoReservaInventario.ACTIVA;
            case "CONFIRMED" -> EstadoReservaInventario.CONFIRMADA;
            case "RELEASED" -> EstadoReservaInventario.LIBERADA;
            case "CANCELLED" -> EstadoReservaInventario.CANCELADA;
            case "EXPIRED" -> EstadoReservaInventario.EXPIRADA;
            default -> throw new IllegalArgumentException("Estado de reserva no válido: " + valor);
        };
    }
}
