package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.request.ProductoRequest;
import com.nexasupplyai.inventory.dto.response.ProductoResponse;
import com.nexasupplyai.inventory.entity.Categoria;
import com.nexasupplyai.inventory.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductoMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "sku", source = "request.sku")
    @Mapping(target = "codigoBarras", source = "request.codigoBarras")
    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "descripcion", source = "request.descripcion")
    @Mapping(target = "unidadMedida", source = "request.unidadMedida")
    @Mapping(target = "precioVenta", source = "request.precioVenta")
    @Mapping(target = "stockMinimo", source = "request.stockMinimo")
    @Mapping(target = "puntoReorden", source = "request.puntoReorden")
    @Mapping(target = "cantidadReorden", source = "request.cantidadReorden")
    @Mapping(target = "activo", source = "request.activo", defaultValue = "true")
    @Mapping(target = "atributos", source = "request.atributos")
    @Mapping(target = "categoria", source = "categoria")
    Producto aEntidad(ProductoRequest request, Categoria categoria);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "sku", source = "request.sku")
    @Mapping(target = "codigoBarras", source = "request.codigoBarras")
    @Mapping(target = "nombre", source = "request.nombre")
    @Mapping(target = "descripcion", source = "request.descripcion")
    @Mapping(target = "unidadMedida", source = "request.unidadMedida")
    @Mapping(target = "precioVenta", source = "request.precioVenta")
    @Mapping(target = "stockMinimo", source = "request.stockMinimo")
    @Mapping(target = "puntoReorden", source = "request.puntoReorden")
    @Mapping(target = "cantidadReorden", source = "request.cantidadReorden")
    @Mapping(target = "activo", source = "request.activo", defaultValue = "true")
    @Mapping(target = "atributos", source = "request.atributos")
    @Mapping(target = "categoria", source = "categoria")
    void actualizarEntidad(ProductoRequest request, Categoria categoria, @MappingTarget Producto producto);

    @Mapping(target = "idCategoria", source = "categoria.id")
    ProductoResponse aResponse(Producto producto);
}
