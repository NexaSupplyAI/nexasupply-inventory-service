package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    Optional<Categoria> findByNombre(String nombre);

    Optional<Categoria> findBySlug(String slug);

    boolean existsByNombre(String nombre);

    boolean existsBySlug(String slug);

    List<Categoria> findAllByActivoTrue();
}
