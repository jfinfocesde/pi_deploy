package com.example.demo_basic.controller;

import com.example.demo_basic.dto.AdoptanteCreateDTO;
import com.example.demo_basic.dto.AdoptanteDTO;
import com.example.demo_basic.service.AdoptanteService;
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
@RequestMapping("/api/adoptantes")
@Tag(name = "Adoptantes", description = "Gestión de adoptantes")
public class AdoptanteController {

    @Autowired
    private AdoptanteService adoptanteService;

    @Operation(summary = "Obtener todos los adoptantes")
    @GetMapping
    public List<AdoptanteDTO> getAll() {
        return adoptanteService.findAll();
    }

    @Operation(summary = "Obtener un adoptante por ID")
    @GetMapping("/{id}")
    public AdoptanteDTO getById(@PathVariable Long id) {
        return adoptanteService.findById(id);
    }

    @Operation(summary = "Crear un adoptante")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptanteDTO create(@RequestBody AdoptanteCreateDTO request) {
        return adoptanteService.save(request);
    }

    @Operation(summary = "Actualizar un adoptante")
    @PutMapping("/{id}")
    public AdoptanteDTO update(@PathVariable Long id, @RequestBody AdoptanteCreateDTO request) {
        return adoptanteService.update(id, request);
    }

    @Operation(summary = "Eliminar un adoptante")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        adoptanteService.delete(id);
    }
}
