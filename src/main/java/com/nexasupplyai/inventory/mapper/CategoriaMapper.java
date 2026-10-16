package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.request.CategoriaRequest;
import com.nexasupplyai.inventory.dto.response.CategoriaResponse;
import com.nexasupplyai.inventory.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoriaMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "slug", source = "request.slug")
    @Mapping(target = "descripcion", source = "request.descripcion")
    @Mapping(target = "activo", source = "request.activo", defaultValue = "true")
    @Mapping(target = "categoriaPadre", source = "categoriaPadre")
    Categoria aEntidad(CategoriaRequest request, Categoria categoriaPadre);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "slug", source = "request.slug")
    @Mapping(target = "descripcion", source = "request.descripcion")
    @Mapping(target = "activo", source = "request.activo", defaultValue = "true")
    @Mapping(target = "categoriaPadre", source = "categoriaPadre")
    void actualizarEntidad(CategoriaRequest request, Categoria categoriaPadre, @MappingTarget Categoria categoria);

    @Mapping(target = "idCategoriaPadre", source = "categoriaPadre.id")
    CategoriaResponse aResponse(Categoria categoria);
}
