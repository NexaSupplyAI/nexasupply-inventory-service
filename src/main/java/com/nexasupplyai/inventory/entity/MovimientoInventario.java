package com.nexasupplyai.inventory.entity;

import com.nexasupplyai.inventory.enums.TipoMovimientoInventario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "inventory_movements", schema = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class MovimientoInventario {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private TipoMovimientoInventario tipoMovimiento;

    @Column(name = "quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "balance_before", nullable = false, precision = 14, scale = 3)
    private BigDecimal saldoAnterior;

    @Column(name = "balance_after", nullable = false, precision = 14, scale = 3)
    private BigDecimal saldoPosterior;

    @Column(name = "reference_type", length = 40)
    private String tipoReferencia;

    @Column(name = "reference_id")
    private UUID idReferencia;

    @Column(name = "idempotency_key", unique = true, length = 120)
    private String claveIdempotencia;

    @Column(name = "reason", length = 300)
    private String motivo;

    @Column(name = "actor_id", length = 120)
    private String idActor;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> metadatos = Map.of();

    @CreationTimestamp
    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime ocurrioEn;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;
}
