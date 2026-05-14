package com.example.demo_basic.controller;

import com.example.demo_basic.dto.SolicitudCreateDTO;
import com.example.demo_basic.dto.SolicitudDTO;
import com.example.demo_basic.model.enums.EstadoSolicitud;
import com.example.demo_basic.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de adopción")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @Operation(summary = "Obtener todas las solicitudes")
    @GetMapping
    public List<SolicitudDTO> getAll() {
        return solicitudService.findAll();
    }

    @Operation(summary = "Obtener una solicitud por ID")
    @GetMapping("/{id}")
    public SolicitudDTO getById(@PathVariable Long id) {
        return solicitudService.findById(id);
    }

    @Operation(summary = "Filtrar solicitudes por estado")
    @GetMapping("/estado/{estado}")
    public List<SolicitudDTO> getByEstado(@PathVariable EstadoSolicitud estado) {
        return solicitudService.findByEstado(estado);
    }

    @Operation(summary = "Obtener solicitudes por adoptante")
    @GetMapping("/adoptante/{adoptanteId}")
    public List<SolicitudDTO> getByAdoptante(@PathVariable Long adoptanteId) {
        return solicitudService.findByAdoptante(adoptanteId);
    }

    @Operation(summary = "Crear una solicitud", description = "Solo requiere mascotaId y adoptanteId en el body")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudDTO create(@RequestBody SolicitudCreateDTO request) {
        return solicitudService.crearSolicitud(request);
    }

    @Operation(summary = "Cambiar estado de una solicitud")
    @PatchMapping("/{id}/estado/{estado}")
    public SolicitudDTO cambiarEstado(@PathVariable Long id, @PathVariable EstadoSolicitud estado) {
        return solicitudService.cambiarEstado(id, estado);
    }

    @Operation(summary = "Eliminar una solicitud")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        solicitudService.delete(id);
    }
}
