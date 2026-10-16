package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.ReservaInventarioRequest;
import com.nexasupplyai.inventory.dto.response.ReservaInventarioResponse;
import com.nexasupplyai.inventory.service.interfaces.ReservaInventarioServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<ReservaInventarioResponse> crear(@Valid @RequestBody ReservaInventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaInventarioService.crear(request));
    }

    @PutMapping("/{id}")
    public ReservaInventarioResponse actualizar(@PathVariable UUID id,
                                                 @Valid @RequestBody ReservaInventarioRequest request) {
        return reservaInventarioService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        reservaInventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
