package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.ReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.entity.Producto;
import com.nexasupplyai.inventory.entity.ReservaInventario;
import com.nexasupplyai.inventory.mapper.ReservaInventarioMapper;
import com.nexasupplyai.inventory.repository.AlmacenRepository;
import com.nexasupplyai.inventory.repository.ProductoRepository;
import com.nexasupplyai.inventory.repository.ReservaInventarioRepository;
import com.nexasupplyai.inventory.service.interfaces.ReservaInventarioServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservaInventarioServiceImpl implements ReservaInventarioServiceInterface {
    private final ReservaInventarioRepository reservaRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final ReservaInventarioMapper reservaMapper;

    public List<ReservaInventarioResponse> listarPorOrden(UUID idOrdenExterna) {
        return reservaRepository.findAllByIdOrdenExterna(idOrdenExterna).stream().map(reservaMapper::aResponse).toList();
    }

    public ReservaInventarioResponse obtenerPorId(UUID id) {
        return reservaMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public ReservaInventarioResponse crear(ReservaInventarioRequest request) {
        ReservaInventario reserva = reservaMapper.aEntidad(
                request, buscarAlmacen(request.idAlmacen()), buscarProducto(request.idProducto())
        );
        return reservaMapper.aResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaInventarioResponse actualizar(UUID id, ReservaInventarioRequest request) {
        ReservaInventario reserva = buscarEntidad(id);
        reservaMapper.actualizarEntidad(
                request, buscarAlmacen(request.idAlmacen()), buscarProducto(request.idProducto()), reserva
        );
        return reservaMapper.aResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public void eliminar(UUID id) {
        reservaRepository.delete(buscarEntidad(id));
    }

    private ReservaInventario buscarEntidad(UUID id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva de inventario no encontrada: " + id));
    }

    private Almacen buscarAlmacen(UUID id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Almacén no encontrado: " + id));
    }

    private Producto buscarProducto(UUID id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
    }
}
