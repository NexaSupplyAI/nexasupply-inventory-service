package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;
import com.nexasupplyai.inventory.entity.ReservaInventario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservaInventarioMapper {
    @Mapping(target = "idAlmacen", source = "almacen.id")
    @Mapping(target = "idProducto", source = "producto.id")
    ReservaInventarioResponse aResponse(ReservaInventario reserva);
}
