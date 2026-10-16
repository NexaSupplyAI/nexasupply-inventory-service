package com.nexasupplyai.inventory.mapper;

import com.nexasupplyai.inventory.dto.request.MovimientoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.entity.MovimientoInventario;
import com.nexasupplyai.inventory.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoInventarioMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "tipoMovimiento", source = "request.tipoMovimiento")
    @Mapping(target = "cantidad", source = "request.cantidad")
    @Mapping(target = "saldoAnterior", source = "request.saldoAnterior")
    @Mapping(target = "saldoPosterior", source = "request.saldoPosterior")
    @Mapping(target = "tipoReferencia", source = "request.tipoReferencia")
    @Mapping(target = "idReferencia", source = "request.idReferencia")
    @Mapping(target = "claveIdempotencia", source = "request.claveIdempotencia")
    @Mapping(target = "motivo", source = "request.motivo")
    @Mapping(target = "idActor", source = "request.idActor")
    @Mapping(target = "metadatos", source = "request.metadatos", defaultExpression = "java(java.util.Map.of())")
    @Mapping(target = "ocurrioEn", source = "request.ocurrioEn")
    @Mapping(target = "almacen", source = "almacen")
    @Mapping(target = "producto", source = "producto")
    MovimientoInventario aEntidad(MovimientoInventarioRequest request, Almacen almacen, Producto producto);

    @Mapping(target = "idAlmacen", source = "almacen.id")
    @Mapping(target = "idProducto", source = "producto.id")
    MovimientoInventarioResponse aResponse(MovimientoInventario movimiento);
}
