package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, UUID> {

    Optional<Almacen> findByCodigo(String codigo);

    List<Almacen> findAllByActivoTrue();
}
