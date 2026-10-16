package com.nexasupplyai.inventory.entity;

import com.nexasupplyai.inventory.enums.EstadoReservaInventario;
import com.nexasupplyai.inventory.enums.EstadoReservaInventarioConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservations", schema = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class ReservaInventario {

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

    @Column(name = "external_order_id", nullable = false)
    private UUID idOrdenExterna;

    @Column(name = "quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidad;

    @Convert(converter = EstadoReservaInventarioConverter.class)
    @Column(name = "status", nullable = false, length = 20)
    private EstadoReservaInventario estado = EstadoReservaInventario.ACTIVA;

    @Column(name = "expires_at")
    private OffsetDateTime expiraEn;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime actualizadoEn;
}
