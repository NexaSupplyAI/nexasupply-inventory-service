package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.AlmacenRequest;
import com.nexasupplyai.inventory.dto.response.AlmacenResponse;
import com.nexasupplyai.inventory.entity.Almacen;
import com.nexasupplyai.inventory.mapper.AlmacenMapper;
import com.nexasupplyai.inventory.repository.AlmacenRepository;
import com.nexasupplyai.inventory.service.interfaces.AlmacenServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlmacenServiceImpl implements AlmacenServiceInterface {
    private final AlmacenRepository almacenRepository;
    private final AlmacenMapper almacenMapper;

    public List<AlmacenResponse> listar(boolean soloActivos) {
        List<Almacen> almacenes = soloActivos
                ? almacenRepository.findAllByActivoTrue()
                : almacenRepository.findAll();
        return almacenes.stream().map(almacenMapper::aResponse).toList();
    }

    public AlmacenResponse obtenerPorId(UUID id) {
        return almacenMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public AlmacenResponse crear(AlmacenRequest request) {
        validarCodigoUnico(request.codigo(), null);
        return almacenMapper.aResponse(almacenRepository.save(almacenMapper.aEntidad(request)));
    }

    @Transactional
    public AlmacenResponse actualizar(UUID id, AlmacenRequest request) {
        Almacen almacen = buscarEntidad(id);
        validarCodigoUnico(request.codigo(), id);
        almacenMapper.actualizarEntidad(request, almacen);
        return almacenMapper.aResponse(almacenRepository.save(almacen));
    }

    @Transactional
    public void eliminar(UUID id) {
        almacenRepository.delete(buscarEntidad(id));
    }

    private Almacen buscarEntidad(UUID id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Almacén no encontrado: " + id));
    }

    private void validarCodigoUnico(String codigo, UUID idActual) {
        almacenRepository.findByCodigo(codigo)
                .filter(almacen -> !almacen.getId().equals(idActual))
                .ifPresent(almacen -> { throw new IllegalArgumentException("El código de almacén ya existe"); });
    }
}
