package com.example.demo_basic.dto;

public record AdoptanteDTO(
    Long id,
    String nombre,
    String identificacion,
    Integer edad,
    Boolean tienePatio
) {}
