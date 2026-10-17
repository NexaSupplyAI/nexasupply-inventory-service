package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;
import com.nexasupplyai.inventory.exception.ReservaInventarioNoEncontradaException;
import com.nexasupplyai.inventory.mapper.ReservaInventarioMapper;
import com.nexasupplyai.inventory.repository.ReservaInventarioRepository;
import com.nexasupplyai.inventory.service.interfaces.ReservaInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservaInventarioServiceImpl implements ReservaInventarioServiceInterface {
    private final ReservaInventarioRepository repository;
    private final ReservaInventarioMapper mapper;

    public List<ReservaInventarioResponse> listarPorOrden(UUID externalOrderId) {
        return repository.findAllByIdOrdenExterna(externalOrderId).stream().map(mapper::aResponse).toList();
    }

    public ReservaInventarioResponse obtenerPorId(UUID id) {
        return repository.findById(id).map(mapper::aResponse)
                .orElseThrow(() -> new ReservaInventarioNoEncontradaException(id));
    }
}
