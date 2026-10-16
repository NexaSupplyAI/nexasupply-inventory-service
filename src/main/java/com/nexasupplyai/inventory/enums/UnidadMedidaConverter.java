package com.nexasupplyai.inventory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UnidadMedidaConverter implements AttributeConverter<UnidadMedida, String> {
    @Override
    public String convertToDatabaseColumn(UnidadMedida atributo) {
        if (atributo == null) return null;
        return switch (atributo) {
            case UND -> "UNIT";
            case KG -> "KG";
            case G -> "G";
            case L -> "L";
            case ML -> "ML";
            case M -> "M";
            case CM -> "CM";
            case BOX -> "BOX";
            case PACK -> "PACK";
        };
    }

    @Override
    public UnidadMedida convertToEntityAttribute(String valor) {
        if (valor == null) return null;
        return switch (valor) {
            case "UNIT" -> UnidadMedida.UND;
            case "KG" -> UnidadMedida.KG;
            case "G" -> UnidadMedida.G;
            case "L" -> UnidadMedida.L;
            case "ML" -> UnidadMedida.ML;
            case "M" -> UnidadMedida.M;
            case "CM" -> UnidadMedida.CM;
            case "BOX" -> UnidadMedida.BOX;
            case "PACK" -> UnidadMedida.PACK;
            default -> throw new IllegalArgumentException("Unidad de medida no válida: " + valor);
        };
    }
}
