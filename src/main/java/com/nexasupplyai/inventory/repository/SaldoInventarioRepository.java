package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.SaldoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaldoInventarioRepository extends JpaRepository<SaldoInventario, UUID> {

    Optional<SaldoInventario> findByAlmacenIdAndProductoId(UUID idAlmacen, UUID idProducto);

    List<SaldoInventario> findAllByProductoId(UUID idProducto);

    List<SaldoInventario> findAllByAlmacenId(UUID idAlmacen);
}
