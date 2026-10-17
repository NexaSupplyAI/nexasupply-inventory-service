package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;
import com.nexasupplyai.inventory.entity.SaldoInventario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaldoInventarioMapper {
    @Mapping(target = "idAlmacen", source = "almacen.id")
    @Mapping(target = "idProducto", source = "producto.id")
    SaldoInventarioResponse aResponse(SaldoInventario saldo);
}
