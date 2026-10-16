package com.nexasupplyai.inventory.service.impl;

import com.nexasupplyai.inventory.dto.request.CategoriaRequest;
import com.nexasupplyai.inventory.dto.response.CategoriaResponse;
import com.nexasupplyai.inventory.entity.Categoria;
import com.nexasupplyai.inventory.mapper.CategoriaMapper;
import com.nexasupplyai.inventory.repository.CategoriaRepository;
import com.nexasupplyai.inventory.service.interfaces.CategoriaServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaServiceInterface {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public List<CategoriaResponse> listar(boolean soloActivas) {
        List<Categoria> categorias = soloActivas ? categoriaRepository.findAllByActivoTrue() : categoriaRepository.findAll();
        return categorias.stream().map(categoriaMapper::aResponse).toList();
    }

    public CategoriaResponse obtenerPorId(UUID id) {
        return categoriaMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        validarUnicidad(request, null);
        Categoria padre = resolverPadre(request.idCategoriaPadre(), null);
        return categoriaMapper.aResponse(categoriaRepository.save(categoriaMapper.aEntidad(request, padre)));
    }

    @Transactional
    public CategoriaResponse actualizar(UUID id, CategoriaRequest request) {
        Categoria categoria = buscarEntidad(id);
        validarUnicidad(request, id);
        Categoria padre = resolverPadre(request.idCategoriaPadre(), id);
        categoriaMapper.actualizarEntidad(request, padre, categoria);
        return categoriaMapper.aResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(UUID id) {
        categoriaRepository.delete(buscarEntidad(id));
    }

    private Categoria buscarEntidad(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + id));
    }

    private Categoria resolverPadre(UUID idPadre, UUID idCategoria) {
        if (idPadre == null) return null;
        if (idPadre.equals(idCategoria)) throw new IllegalArgumentException("Una categoría no puede ser su propia padre");
        return buscarEntidad(idPadre);
    }

    private void validarUnicidad(CategoriaRequest request, UUID idActual) {
        categoriaRepository.findByNombre(request.nombre()).filter(c -> !c.getId().equals(idActual))
                .ifPresent(c -> { throw new IllegalArgumentException("El nombre de categoría ya existe"); });
        categoriaRepository.findBySlug(request.slug()).filter(c -> !c.getId().equals(idActual))
                .ifPresent(c -> { throw new IllegalArgumentException("El slug de categoría ya existe"); });
    }
}
