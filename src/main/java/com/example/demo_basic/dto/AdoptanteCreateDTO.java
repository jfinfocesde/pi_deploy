package com.example.demo_basic.dto;

public record AdoptanteCreateDTO(
    String nombre,
    String identificacion,
    Integer edad,
    Boolean tienePatio
) {}
