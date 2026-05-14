package com.example.demo_basic.controller;

import com.example.demo_basic.dto.MascotaCreateDTO;
import com.example.demo_basic.dto.MascotaDTO;
import com.example.demo_basic.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@Tag(name = "Mascotas", description = "Gestión de mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Operation(summary = "Obtener todas las mascotas")
    @GetMapping
    public List<MascotaDTO> getAll() {
        return mascotaService.findAll();
    }

    @Operation(summary = "Obtener una mascota por ID")
    @GetMapping("/{id}")
    public MascotaDTO getById(@PathVariable Long id) {
        return mascotaService.findById(id);
    }

    @Operation(summary = "Crear una mascota")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MascotaDTO create(@RequestBody MascotaCreateDTO request) {
        return mascotaService.save(request);
    }

    @Operation(summary = "Actualizar una mascota")
    @PutMapping("/{id}")
    public MascotaDTO update(@PathVariable Long id, @RequestBody MascotaCreateDTO request) {
        return mascotaService.update(id, request);
    }

    @Operation(summary = "Eliminar una mascota")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mascotaService.delete(id);
    }
}
