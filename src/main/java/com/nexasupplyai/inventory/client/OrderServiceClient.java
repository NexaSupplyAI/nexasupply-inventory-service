package com.nexasupplyai.inventory.client;

import com.nexasupplyai.inventory.dto.external.OrdenExternaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class OrderServiceClient {

    private final RestClient clienteHttp;

    public OrderServiceClient(
            RestClient.Builder constructorCliente,
            @Value("${clients.order-service.base-url}") String urlBaseOrderService
    ) {
        this.clienteHttp = constructorCliente.baseUrl(urlBaseOrderService).build();
    }

    public OrdenExternaResponse obtenerOrden(UUID idOrden) {
        return clienteHttp.get()
                .uri("/api/v1/ordenes/{id}", idOrden)
                .retrieve()
                .body(OrdenExternaResponse.class);
    }
}
