package com.nexasupplyai.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "inventory_balances",
        schema = "inventory",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_inventory_balance",
                columnNames = {"warehouse_id", "product_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class SaldoInventario {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto producto;

    @Column(name = "on_hand_quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadEnMano = BigDecimal.ZERO;

    @Column(name = "reserved_quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadReservada = BigDecimal.ZERO;

    @Column(name = "available_quantity", precision = 14, scale = 3, insertable = false, updatable = false)
    private BigDecimal cantidadDisponible;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime actualizadoEn;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
