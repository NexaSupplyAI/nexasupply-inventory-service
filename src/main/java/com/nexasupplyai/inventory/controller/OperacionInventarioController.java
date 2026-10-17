package com.nexasupplyai.inventory.controller;

import com.nexasupplyai.inventory.dto.request.EntradaInventarioRequest;
import com.nexasupplyai.inventory.dto.request.ReservaOrdenRequest;
import com.nexasupplyai.inventory.dto.response.MovimientoInventarioResponse;
import com.nexasupplyai.inventory.dto.response.OperacionInventarioResponse;
import com.nexasupplyai.inventory.service.interfaces.OperacionInventarioServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operaciones-inventario")
@RequiredArgsConstructor
public class OperacionInventarioController {
    private final OperacionInventarioServiceInterface servicio;

    @PostMapping("/reservas")
    public ResponseEntity<OperacionInventarioResponse> reservar(@Valid @RequestBody ReservaOrdenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.reservar(request));
    }

    @PostMapping("/ordenes/{idOrden}/consumir")
    public OperacionInventarioResponse consumir(@PathVariable UUID idOrden) {
        return servicio.consumir(idOrden);
    }

    @PostMapping("/ordenes/{idOrden}/liberar")
    public OperacionInventarioResponse liberar(@PathVariable UUID idOrden) {
        return servicio.liberar(idOrden);
    }

    @PostMapping("/entradas")
    public ResponseEntity<MovimientoInventarioResponse> registrarEntrada(
            @Valid @RequestBody EntradaInventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.registrarEntrada(request));
    }
}
