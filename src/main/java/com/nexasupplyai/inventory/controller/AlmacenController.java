package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.AlmacenRequest;
import com.nexasupplyai.inventory.dto.response.AlmacenResponse;
import com.nexasupplyai.inventory.service.interfaces.AlmacenServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/almacenes")
@RequiredArgsConstructor
public class AlmacenController {
    private final AlmacenServiceInterface almacenService;

    @GetMapping
    public List<AlmacenResponse> listar(@RequestParam(defaultValue = "true") boolean soloActivos) {
        return almacenService.listar(soloActivos);
    }

    @GetMapping("/{id}")
    public AlmacenResponse obtenerPorId(@PathVariable UUID id) {
        return almacenService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlmacenResponse> crear(@Valid @RequestBody AlmacenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(almacenService.crear(request));
    }

    @PutMapping("/{id}")
    public AlmacenResponse actualizar(@PathVariable UUID id, @Valid @RequestBody AlmacenRequest request) {
        return almacenService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        almacenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
