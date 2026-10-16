package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.request.ReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.entity.Producto;
import com.nexasupplyai.inventory.entity.ReservaInventario;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReservaInventarioMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idOrdenExterna", source = "request.idOrdenExterna")
    @Mapping(target = "cantidad", source = "request.cantidad")
    @Mapping(target = "estado", source = "request.estado", defaultValue = "ACTIVA")
    @Mapping(target = "expiraEn", source = "request.expiraEn")
    @Mapping(target = "almacen", source = "almacen")
    @Mapping(target = "producto", source = "producto")
    ReservaInventario aEntidad(ReservaInventarioRequest request, Almacen almacen, Producto producto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idOrdenExterna", source = "request.idOrdenExterna")
    @Mapping(target = "cantidad", source = "request.cantidad")
    @Mapping(target = "estado", source = "request.estado", defaultValue = "ACTIVA")
    @Mapping(target = "expiraEn", source = "request.expiraEn")
    @Mapping(target = "almacen", source = "almacen")
    @Mapping(target = "producto", source = "producto")
    void actualizarEntidad(ReservaInventarioRequest request, Almacen almacen, Producto producto,
                           @MappingTarget ReservaInventario reserva);

    @Mapping(target = "idAlmacen", source = "almacen.id")
    @Mapping(target = "idProducto", source = "producto.id")
    ReservaInventarioResponse aResponse(ReservaInventario reserva);
}
