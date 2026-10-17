package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.ReservaInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.LockModeType;

@Repository
public interface ReservaInventarioRepository extends JpaRepository<ReservaInventario, UUID> {

    List<ReservaInventario> findAllByIdOrdenExterna(UUID idOrdenExterna);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select reserva from ReservaInventario reserva
            where reserva.idOrdenExterna = :idOrdenExterna
            order by reserva.producto.id, reserva.almacen.id
            """)
    List<ReservaInventario> bloquearTodasPorOrden(UUID idOrdenExterna);

}
