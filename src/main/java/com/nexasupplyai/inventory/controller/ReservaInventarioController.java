package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;
import com.nexasupplyai.inventory.service.interfaces.ReservaInventarioServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas-inventario")
@RequiredArgsConstructor
public class ReservaInventarioController {
    private final ReservaInventarioServiceInterface reservaInventarioService;

    @GetMapping
    public List<ReservaInventarioResponse> listarPorOrden(@RequestParam UUID idOrdenExterna) {
        return reservaInventarioService.listarPorOrden(idOrdenExterna);
    }

    @GetMapping("/{id}")
    public ReservaInventarioResponse obtenerPorId(@PathVariable UUID id) {
        return reservaInventarioService.obtenerPorId(id);
    }
}
