package com.example.demo_basic.service;

import com.example.demo_basic.dto.AdoptanteCreateDTO;
import com.example.demo_basic.dto.AdoptanteDTO;
import com.example.demo_basic.model.entity.Adoptante;
import com.example.demo_basic.repository.AdoptanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdoptanteService {

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    public List<AdoptanteDTO> findAll() {
        return adoptanteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AdoptanteDTO findById(Long id) {
        return convertToDTO(findEntity(id));
    }

    @Transactional
    public AdoptanteDTO save(AdoptanteCreateDTO request) {
        Adoptante adoptante = convertToEntity(request);
        return convertToDTO(adoptanteRepository.save(adoptante));
    }

    @Transactional
    public AdoptanteDTO update(Long id, AdoptanteCreateDTO request) {
        Adoptante adoptante = findEntity(id);
        adoptante.setNombre(request.nombre());
        adoptante.setIdentificacion(request.identificacion());
        adoptante.setEdad(request.edad());
        adoptante.setTienePatio(request.tienePatio());
        return convertToDTO(adoptanteRepository.save(adoptante));
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        adoptanteRepository.deleteById(id);
    }

    // --- Mapeo Manual ---

    private AdoptanteDTO convertToDTO(Adoptante adoptante) {
        return new AdoptanteDTO(
                adoptante.getId(),
                adoptante.getNombre(),
                adoptante.getIdentificacion(),
                adoptante.getEdad(),
                adoptante.getTienePatio()
        );
    }

    private Adoptante convertToEntity(AdoptanteCreateDTO dto) {
        Adoptante adoptante = new Adoptante();
        adoptante.setNombre(dto.nombre());
        adoptante.setIdentificacion(dto.identificacion());
        adoptante.setEdad(dto.edad());
        adoptante.setTienePatio(dto.tienePatio());
        return adoptante;
    }

    public Adoptante findEntity(Long id) {
        return adoptanteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Adoptante no encontrado con id: " + id));
    }
}
