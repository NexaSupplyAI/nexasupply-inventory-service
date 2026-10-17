package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.entity.MovimientoInventario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoInventarioMapper {
    @Mapping(target = "idAlmacen", source = "almacen.id")
    @Mapping(target = "idProducto", source = "producto.id")
    MovimientoInventarioResponse aResponse(MovimientoInventario movimiento);
}
