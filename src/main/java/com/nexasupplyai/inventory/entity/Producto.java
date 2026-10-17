package com.nexasupplyai.inventory.entity;

import com.nexasupplyai.inventory.enums.UnidadMedida;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "sku", nullable = false, unique = true, length = 80)
    private String sku;

    @Column(name = "barcode", unique = true, length = 80)
    private String codigoBarras;

    @Column(name = "name", nullable = false, length = 180)
    private String nombre;

    @Column(name = "description", columnDefinition = "text")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private UnidadMedida unidadMedida = UnidadMedida.UNIT;

    @Column(name = "sale_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal precioVenta = BigDecimal.ZERO;

    @Column(name = "minimum_stock", nullable = false, precision = 14, scale = 3)
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    @Column(name = "reorder_point", nullable = false, precision = 14, scale = 3)
    private BigDecimal puntoReorden = BigDecimal.ZERO;

    @Column(name = "reorder_quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidadReorden = BigDecimal.ZERO;

    @Column(name = "active", nullable = false)
    private Boolean activo = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> atributos = Map.of();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime actualizadoEn;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
