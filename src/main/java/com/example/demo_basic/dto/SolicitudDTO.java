package com.example.demo_basic.dto;

import com.example.demo_basic.model.enums.EstadoSolicitud;

public record SolicitudDTO(
    Long id,
    MascotaDTO mascota,
    AdoptanteDTO adoptante,
    EstadoSolicitud estado
) {}
