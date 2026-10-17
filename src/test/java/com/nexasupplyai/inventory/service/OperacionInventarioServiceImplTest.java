package com.nexasupplyai.inventory.service;

import com.nexasupplyai.inventory.dto.request.LineaReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.request.ReservaOrdenRequest;
import com.nexasupplyai.inventory.entity.*;
import com.nexasupplyai.inventory.mapper.MovimientoInventarioMapper;
import com.nexasupplyai.inventory.mapper.ReservaInventarioMapper;
import com.nexasupplyai.inventory.repository.*;
import com.nexasupplyai.inventory.service.impl.OperacionInventarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperacionInventarioServiceImplTest {
    @Mock SaldoInventarioRepository saldos;
    @Mock ReservaInventarioRepository reservas;
    @Mock MovimientoInventarioRepository movimientos;
    @Mock AlmacenRepository almacenes;
    @Mock ProductoRepository productos;
    @Mock ReservaInventarioMapper reservaMapper;
    @Mock MovimientoInventarioMapper movimientoMapper;

    @Test
    void reservaStockDeFormaAtomica() {
        UUID orden = UUID.randomUUID(), almacenId = UUID.randomUUID(), productoId = UUID.randomUUID();
        Almacen almacen = new Almacen(); almacen.setId(almacenId); almacen.setActivo(true);
        Producto producto = new Producto(); producto.setId(productoId); producto.setActivo(true); producto.setSku("SKU-1");
        SaldoInventario saldo = new SaldoInventario(); saldo.setAlmacen(almacen); saldo.setProducto(producto);
        saldo.setCantidadEnMano(new BigDecimal("10")); saldo.setCantidadReservada(BigDecimal.ZERO);
        when(reservas.bloquearTodasPorOrden(orden)).thenReturn(List.of());
        when(saldos.bloquearPorAlmacenYProducto(almacenId, productoId)).thenReturn(Optional.of(saldo));
        when(reservas.save(any())).thenAnswer(i -> i.getArgument(0));
        when(movimientos.save(any())).thenAnswer(i -> i.getArgument(0));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        var respuesta = servicio.reservar(new ReservaOrdenRequest(orden,
                List.of(new LineaReservaInventarioRequest(almacenId, productoId, new BigDecimal("3"))), null));

        assertEquals(new BigDecimal("3"), saldo.getCantidadReservada());
        assertEquals("RESERVED", respuesta.resultado());
        assertFalse(respuesta.idempotente());
        verify(movimientos).save(any());
    }

    @Test
    void rechazaReservaSinStockDisponible() {
        UUID orden = UUID.randomUUID(), almacenId = UUID.randomUUID(), productoId = UUID.randomUUID();
        Almacen almacen = new Almacen(); almacen.setId(almacenId); almacen.setActivo(true);
        Producto producto = new Producto(); producto.setId(productoId); producto.setActivo(true); producto.setSku("SKU-1");
        SaldoInventario saldo = new SaldoInventario(); saldo.setAlmacen(almacen); saldo.setProducto(producto);
        saldo.setCantidadEnMano(new BigDecimal("2")); saldo.setCantidadReservada(BigDecimal.ZERO);
        when(reservas.bloquearTodasPorOrden(orden)).thenReturn(List.of());
        when(saldos.bloquearPorAlmacenYProducto(almacenId, productoId)).thenReturn(Optional.of(saldo));
        var servicio = new OperacionInventarioServiceImpl(saldos, reservas, movimientos, almacenes,
                productos, reservaMapper, movimientoMapper);

        assertThrows(IllegalArgumentException.class, () -> servicio.reservar(new ReservaOrdenRequest(orden,
                List.of(new LineaReservaInventarioRequest(almacenId, productoId, new BigDecimal("3"))), null)));
        verifyNoInteractions(movimientos);
    }
}
