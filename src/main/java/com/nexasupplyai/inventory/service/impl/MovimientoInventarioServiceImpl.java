package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.exception.MovimientoInventarioNoEncontradoException;
import com.nexasupplyai.inventory.mapper.MovimientoInventarioMapper;
import com.nexasupplyai.inventory.repository.MovimientoInventarioRepository;
import com.nexasupplyai.inventory.service.interfaces.MovimientoInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovimientoInventarioServiceImpl implements MovimientoInventarioServiceInterface {
    private final MovimientoInventarioRepository repository;
    private final MovimientoInventarioMapper mapper;

    public List<MovimientoInventarioResponse> listarPorProducto(UUID productId) {
        return repository.findAllByProductoIdOrderByOcurrioEnDesc(productId).stream().map(mapper::aResponse).toList();
    }

    public MovimientoInventarioResponse obtenerPorId(UUID id) {
        return repository.findById(id).map(mapper::aResponse)
                .orElseThrow(() -> new MovimientoInventarioNoEncontradoException(id));
    }
}
