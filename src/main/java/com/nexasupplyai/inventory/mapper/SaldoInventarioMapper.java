package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.request.SaldoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.entity.Producto;
import com.nexasupplyai.inventory.entity.SaldoInventario;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SaldoInventarioMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "cantidadEnMano", source = "request.cantidadEnMano")
    @Mapping(target = "cantidadReservada", source = "request.cantidadReservada")
    @Mapping(target = "almacen", source = "almacen")
    @Mapping(target = "producto", source = "producto")
    SaldoInventario aEntidad(SaldoInventarioRequest request, Almacen almacen, Producto producto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "cantidadEnMano", source = "request.cantidadEnMano")
    @Mapping(target = "cantidadReservada", source = "request.cantidadReservada")
    @Mapping(target = "almacen", source = "almacen")
    @Mapping(target = "producto", source = "producto")
    void actualizarEntidad(SaldoInventarioRequest request, Almacen almacen, Producto producto,
                           @MappingTarget SaldoInventario saldo);

    @Mapping(target = "idAlmacen", source = "almacen.id")
    @Mapping(target = "idProducto", source = "producto.id")
    SaldoInventarioResponse aResponse(SaldoInventario saldo);
}
