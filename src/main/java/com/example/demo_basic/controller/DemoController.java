package com.example.demo_basic.controller;

import com.example.demo_basic.dto.MascotaDTO;
import com.example.demo_basic.dto.SolicitudDTO;
import com.example.demo_basic.mapper.SolicitudMapper;
import com.example.demo_basic.model.entity.Mascota;
import com.example.demo_basic.model.entity.Solicitud;
import com.example.demo_basic.repository.MascotaRepository;
import com.example.demo_basic.repository.SolicitudRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo Comparativa", description = "Endpoints para comparar el uso de Entidades vs DTOs")
public class DemoController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private SolicitudMapper solicitudMapper;

    @Operation(summary = "Mascotas SIN DTOs (Entidades puras)", 
               description = "Muestra todos los campos internos, incluyendo auditoría.")
    @GetMapping("/mascotas-entidad")
    public List<Mascota> getMascotasEntidad() {
        return mascotaRepository.findAll();
    }

    @Operation(summary = "Mascotas CON DTOs", 
               description = "Muestra solo los campos necesarios para el cliente.")
    @GetMapping("/mascotas-dto")
    public List<MascotaDTO> getMascotasDTO() {
        return mascotaRepository.findAll().stream()
                .map(m -> new MascotaDTO(m.getId(), m.getNombre(), m.getEspecie(), m.getEdad(), m.getTamano(), m.getEstado()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Solicitudes SIN DTOs (Entidades puras)", 
               description = "Muestra objetos anidados completos y metadatos innecesarios.")
    @GetMapping("/solicitudes-entidad")
    public List<Solicitud> getSolicitudesEntidad() {
        return solicitudRepository.findAll();
    }

    @Operation(summary = "Solicitudes CON DTOs (MapStruct)", 
               description = "Muestra una estructura limpia y optimizada para el Frontend.")
    @GetMapping("/solicitudes-dto")
    public List<SolicitudDTO> getSolicitudesDTO() {
        return solicitudRepository.findAll().stream()
                .map(solicitudMapper::toDTO)
                .collect(Collectors.toList());
    }
}
