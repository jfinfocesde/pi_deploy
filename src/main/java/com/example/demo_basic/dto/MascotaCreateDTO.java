package com.example.demo_basic.dto;

import com.example.demo_basic.model.enums.EstadoMascota;
import com.example.demo_basic.model.enums.TamanoMascota;

public record MascotaCreateDTO(
    String nombre,
    String especie,
    Integer edad,
    TamanoMascota tamano,
    EstadoMascota estado
) {}
