package com.example.demo_basic.service;

import com.example.demo_basic.dto.MascotaCreateDTO;
import com.example.demo_basic.dto.MascotaDTO;
import com.example.demo_basic.model.entity.Mascota;
import com.example.demo_basic.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<MascotaDTO> findAll() {
        return mascotaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MascotaDTO findById(Long id) {
        return convertToDTO(findEntity(id));
    }

    @Transactional
    public MascotaDTO save(MascotaCreateDTO request) {
        Mascota mascota = convertToEntity(request);
        return convertToDTO(save(mascota));
    }

    @Transactional
    public Mascota save(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    @Transactional
    public MascotaDTO update(Long id, MascotaCreateDTO request) {
        Mascota mascota = findEntity(id);
        mascota.setNombre(request.nombre());
        mascota.setEspecie(request.especie());
        mascota.setEdad(request.edad());
        mascota.setTamano(request.tamano());
        mascota.setEstado(request.estado());
        return convertToDTO(mascotaRepository.save(mascota));
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        mascotaRepository.deleteById(id);
    }

    // --- Mapeo Manual ---

    private MascotaDTO convertToDTO(Mascota mascota) {
        return new MascotaDTO(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getEdad(),
                mascota.getTamano(),
                mascota.getEstado()
        );
    }

    private Mascota convertToEntity(MascotaCreateDTO dto) {
        Mascota mascota = new Mascota();
        mascota.setNombre(dto.nombre());
        mascota.setEspecie(dto.especie());
        mascota.setEdad(dto.edad());
        mascota.setTamano(dto.tamano());
        mascota.setEstado(dto.estado());
        return mascota;
    }

    public Mascota findEntity(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mascota no encontrada con id: " + id));
    }
}
