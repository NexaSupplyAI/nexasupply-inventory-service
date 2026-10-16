package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.CategoriaRequest;
import com.nexasupplyai.inventory.dto.response.CategoriaResponse;
import com.nexasupplyai.inventory.service.interfaces.CategoriaServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaServiceInterface categoriaService;

    @GetMapping
    public List<CategoriaResponse> listar(@RequestParam(defaultValue = "true") boolean soloActivas) {
        return categoriaService.listar(soloActivas);
    }

    @GetMapping("/{id}")
    public CategoriaResponse obtenerPorId(@PathVariable UUID id) {
        return categoriaService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(request));
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(@PathVariable UUID id, @Valid @RequestBody CategoriaRequest request) {
        return categoriaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
