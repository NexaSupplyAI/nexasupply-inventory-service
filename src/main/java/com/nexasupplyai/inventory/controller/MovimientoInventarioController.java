package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.service.interfaces.MovimientoInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
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
}
