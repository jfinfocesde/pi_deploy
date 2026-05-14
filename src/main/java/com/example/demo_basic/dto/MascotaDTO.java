package com.example.demo_basic.dto;

import com.example.demo_basic.model.enums.EstadoMascota;
import com.example.demo_basic.model.enums.TamanoMascota;

public record MascotaDTO(
    Long id,
    String nombre,
    String especie,
    Integer edad,
    TamanoMascota tamano,
    EstadoMascota estado
) {}
