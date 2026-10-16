package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.MovimientoInventarioRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.service.interfaces.MovimientoInventarioServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movimientos-inventario")
@RequiredArgsConstructor
public class MovimientoInventarioController {
    private final MovimientoInventarioServiceInterface movimientoInventarioService;

    @GetMapping
    public List<MovimientoInventarioResponse> listarPorProducto(@RequestParam UUID idProducto) {
        return movimientoInventarioService.listarPorProducto(idProducto);
    }

    @GetMapping("/{id}")
    public MovimientoInventarioResponse obtenerPorId(@PathVariable UUID id) {
        return movimientoInventarioService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<MovimientoInventarioResponse> crear(
            @Valid @RequestBody MovimientoInventarioRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoInventarioService.crear(request));
    }
}
