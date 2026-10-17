package com.nexasupplyai.inventory.repository;

import com.nexasupplyai.inventory.entity.SaldoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;

@Repository
public interface SaldoInventarioRepository extends JpaRepository<SaldoInventario, UUID> {

    Optional<SaldoInventario> findByAlmacenIdAndProductoId(UUID idAlmacen, UUID idProducto);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select saldo from SaldoInventario saldo
            where saldo.almacen.id = :idAlmacen and saldo.producto.id = :idProducto
            """)
    Optional<SaldoInventario> bloquearPorAlmacenYProducto(UUID idAlmacen, UUID idProducto);

    List<SaldoInventario> findAllByProductoId(UUID idProducto);

    List<SaldoInventario> findAllByAlmacenId(UUID idAlmacen);

    @Query("""
            select saldo
            from SaldoInventario saldo
            where saldo.producto.activo = true
              and saldo.almacen.activo = true
              and (saldo.cantidadEnMano - saldo.cantidadReservada) <=
                  case
                    when saldo.producto.stockMinimo > saldo.producto.puntoReorden
                    then saldo.producto.stockMinimo
                    else saldo.producto.puntoReorden
                  end
            order by saldo.producto.nombre, saldo.almacen.nombre
            """)
    List<SaldoInventario> findAllConStockBajo();
}
