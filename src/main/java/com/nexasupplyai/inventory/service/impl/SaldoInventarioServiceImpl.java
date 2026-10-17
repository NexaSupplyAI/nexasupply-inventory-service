package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;
import com.nexasupplyai.inventory.exception.SaldoInventarioNoEncontradoException;
import com.nexasupplyai.inventory.mapper.SaldoInventarioMapper;
import com.nexasupplyai.inventory.repository.SaldoInventarioRepository;
import com.nexasupplyai.inventory.service.interfaces.SaldoInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaldoInventarioServiceImpl implements SaldoInventarioServiceInterface {
    private final SaldoInventarioRepository repository;
    private final SaldoInventarioMapper mapper;

    public List<SaldoInventarioResponse> listar() {
        return repository.findAll().stream().map(mapper::aResponse).toList();
    }

    public List<SaldoInventarioResponse> listarPorProducto(UUID productId) {
        return repository.findAllByProductoId(productId).stream().map(mapper::aResponse).toList();
    }

    public List<SaldoInventarioResponse> listarStockBajo() {
        return repository.findAllConStockBajo().stream().map(mapper::aResponse).toList();
    }

    public SaldoInventarioResponse obtenerPorId(UUID id) {
        return repository.findById(id).map(mapper::aResponse)
                .orElseThrow(() -> new SaldoInventarioNoEncontradoException(id));
    }
}
