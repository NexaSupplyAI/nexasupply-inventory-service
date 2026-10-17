package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, UUID> {

    Optional<MovimientoInventario> findByClaveIdempotencia(String claveIdempotencia);

    List<MovimientoInventario> findAllByProductoIdOrderByOcurrioEnDesc(UUID idProducto);

}
