package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    Optional<Producto> findBySku(String sku);

    Optional<Producto> findByCodigoBarras(String codigoBarras);

    boolean existsBySku(String sku);

    boolean existsByCodigoBarras(String codigoBarras);

    List<Producto> findAllByCategoriaIdAndActivoTrue(UUID idCategoria);
}
