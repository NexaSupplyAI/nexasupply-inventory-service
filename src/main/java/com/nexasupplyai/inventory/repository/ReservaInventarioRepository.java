package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.ReservaInventario;
import com.nexasupplyai.inventory.enums.EstadoReservaInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaInventarioRepository extends JpaRepository<ReservaInventario, UUID> {

    List<ReservaInventario> findAllByIdOrdenExterna(UUID idOrdenExterna);

    List<ReservaInventario> findAllByProductoIdAndEstadoIn(
            UUID idProducto,
            Collection<EstadoReservaInventario> estados
    );

    List<ReservaInventario> findAllByAlmacenIdAndProductoIdAndEstadoIn(
            UUID idAlmacen,
            UUID idProducto,
            Collection<EstadoReservaInventario> estados
    );
}
