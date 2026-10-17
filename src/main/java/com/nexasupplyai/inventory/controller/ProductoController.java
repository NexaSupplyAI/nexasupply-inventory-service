package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.ProductoRequest;
import com.nexasupplyai.inventory.dto.response.ProductoResponse;
import com.nexasupplyai.inventory.service.interfaces.ProductoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoServiceInterface productoService;

    @GetMapping
    public List<ProductoResponse> listarPorCategoria(@RequestParam(required = false) UUID idCategoria) {
        return idCategoria == null ? productoService.listar() : productoService.listarPorCategoria(idCategoria);
    }

    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable UUID id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable UUID id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
