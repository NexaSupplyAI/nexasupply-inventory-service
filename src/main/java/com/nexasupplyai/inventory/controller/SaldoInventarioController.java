package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.SaldoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.SaldoInventarioResponse;
import com.nexasupplyai.inventory.service.interfaces.SaldoInventarioServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/saldos-inventario")
@RequiredArgsConstructor
public class SaldoInventarioController {
    private final SaldoInventarioServiceInterface saldoInventarioService;

    @GetMapping
    public List<SaldoInventarioResponse> listarPorProducto(@RequestParam(required = false) UUID idProducto) {
        return idProducto == null ? saldoInventarioService.listar() : saldoInventarioService.listarPorProducto(idProducto);
    }

    @GetMapping("/stock-bajo")
    public List<SaldoInventarioResponse> listarStockBajo() {
        return saldoInventarioService.listarStockBajo();
    }

    @GetMapping("/{id}")
    public SaldoInventarioResponse obtenerPorId(@PathVariable UUID id) {
        return saldoInventarioService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<SaldoInventarioResponse> crear(@Valid @RequestBody SaldoInventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saldoInventarioService.crear(request));
    }

    @PutMapping("/{id}")
    public SaldoInventarioResponse actualizar(@PathVariable UUID id, @Valid @RequestBody SaldoInventarioRequest request) {
        return saldoInventarioService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        saldoInventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
