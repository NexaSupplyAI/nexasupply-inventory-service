package com.nexasupplyai.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "warehouses", schema = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class Almacen {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 40)
    private String codigo;

    @Column(name = "name", nullable = false, length = 140)
    private String nombre;

    @Column(name = "description", columnDefinition = "text")
    private String descripcion;

    @Column(name = "address_line", length = 220)
    private String direccion;

    @Column(name = "district", length = 120)
    private String distrito;

    @Column(name = "city", length = 120)
    private String ciudad;

    @Column(name = "region", length = 120)
    private String region;

    @Column(name = "country_code", nullable = false, length = 2)
    private String codigoPais = "PE";

    @Column(name = "active", nullable = false)
    private Boolean activo = true;

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
