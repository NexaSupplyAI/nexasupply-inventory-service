package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.request.AlmacenRequest;
import com.nexasupplyai.inventory.dto.response.AlmacenResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AlmacenMapper {
    @Mapping(target = "activo", source = "activo", defaultValue = "true")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "version", ignore = true)
    Almacen aEntidad(AlmacenRequest request);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "version", ignore = true)
    void actualizarEntidad(AlmacenRequest request, @MappingTarget Almacen almacen);
    AlmacenResponse aResponse(Almacen almacen);
}
