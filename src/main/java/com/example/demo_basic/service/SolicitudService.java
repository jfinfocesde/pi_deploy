package com.example.demo_basic.service;

import com.example.demo_basic.dto.SolicitudCreateDTO;
import com.example.demo_basic.dto.SolicitudDTO;
import com.example.demo_basic.mapper.SolicitudMapper;
import com.example.demo_basic.model.entity.Adoptante;
import com.example.demo_basic.model.entity.Mascota;
import com.example.demo_basic.model.entity.Solicitud;
import com.example.demo_basic.model.enums.EstadoMascota;
import com.example.demo_basic.model.enums.EstadoSolicitud;
import com.example.demo_basic.model.enums.TamanoMascota;
import com.example.demo_basic.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private AdoptanteService adoptanteService;

    @Autowired
    private SolicitudMapper solicitudMapper;

    public List<SolicitudDTO> findAll() {
        return solicitudRepository.findAll().stream()
                .map(solicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SolicitudDTO findById(Long id) {
        return solicitudMapper.toDTO(findEntity(id));
    }

    public List<SolicitudDTO> findByEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado).stream()
                .map(solicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> findByAdoptante(Long adoptanteId) {
        return solicitudRepository.findByAdoptanteId(adoptanteId).stream()
                .map(solicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SolicitudDTO crearSolicitud(SolicitudCreateDTO request) {
        Mascota mascota = mascotaService.findEntity(request.mascotaId());
        Adoptante adoptante = adoptanteService.findEntity(request.adoptanteId());

        validarMascotaDisponible(mascota);
        validarMayorEdad(adoptante);
        validarMaximoSolicitudesActivas(adoptante);
        validarPatioSiEsGrande(mascota, adoptante);

        Solicitud solicitud = new Solicitud();
        solicitud.setMascota(mascota);
        solicitud.setAdoptante(adoptante);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        mascota.setEstado(EstadoMascota.EN_PROCESO);
        mascotaService.save(mascota);

        return solicitudMapper.toDTO(solicitudRepository.save(solicitud));
    }

    @Transactional
    public SolicitudDTO cambiarEstado(Long id, EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = findEntity(id);
        Mascota mascota = mascotaService.findEntity(solicitud.getMascota().getId());

        solicitud.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoSolicitud.APROBADA) {
            mascota.setEstado(EstadoMascota.ADOPTADO);
            mascotaService.save(mascota);
        }

        if (nuevoEstado == EstadoSolicitud.RECHAZADA) {
            mascota.setEstado(EstadoMascota.DISPONIBLE);
            mascotaService.save(mascota);
        }

        return solicitudMapper.toDTO(solicitudRepository.save(solicitud));
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        solicitudRepository.deleteById(id);
    }

    private void validarMascotaDisponible(Mascota mascota) {
        if (mascota.getEstado() != EstadoMascota.DISPONIBLE) {
            throw new IllegalArgumentException("La mascota debe estar DISPONIBLE para crear la solicitud.");
        }
    }

    private void validarMayorEdad(Adoptante adoptante) {
        if (adoptante.getEdad() == null || adoptante.getEdad() <= 18) {
            throw new IllegalArgumentException("El adoptante debe ser mayor de 18 años.");
        }
    }

    private void validarMaximoSolicitudesActivas(Adoptante adoptante) {
        long activas = solicitudRepository.countByAdoptanteIdAndEstado(adoptante.getId(), EstadoSolicitud.PENDIENTE);
        if (activas >= 2) {
            throw new IllegalArgumentException("El adoptante ya tiene 2 solicitudes activas.");
        }
    }

    private void validarPatioSiEsGrande(Mascota mascota, Adoptante adoptante) {
        if (mascota.getTamano() == TamanoMascota.GRANDE && !Boolean.TRUE.equals(adoptante.getTienePatio())) {
            throw new IllegalArgumentException("Para una mascota GRANDE el adoptante debe tener patio.");
        }
    }

    private Solicitud findEntity(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id: " + id));
    }
}
