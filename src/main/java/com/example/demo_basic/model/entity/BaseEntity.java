package com.example.demo_basic.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase base para todas las entidades del sistema.
 * Utiliza @MappedSuperclass para que sus campos sean heredados
 * por las entidades hijas, sin crear una tabla propia en la BD.
 *
 * La auditoría automática es gestionada por Spring Data JPA:
 *   - @CreatedDate    → asigna la fecha al insertar (INSERT).
 *   - @LastModifiedDate → actualiza la fecha en cada modificación (UPDATE).
 * Requiere @EnableJpaAuditing en la clase principal de la aplicación.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
