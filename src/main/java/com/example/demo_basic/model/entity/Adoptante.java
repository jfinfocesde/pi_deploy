package com.example.demo_basic.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "adoptantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Adoptante extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "identificacion", nullable = false, length = 40, unique = true)
    private String identificacion;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "tiene_patio", nullable = false)
    private Boolean tienePatio;
}
