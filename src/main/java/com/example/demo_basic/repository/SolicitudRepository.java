package com.example.demo_basic.repository;

import com.example.demo_basic.model.entity.Solicitud;
import com.example.demo_basic.model.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    long countByAdoptanteIdAndEstado(Long adoptanteId, EstadoSolicitud estado);

    List<Solicitud> findByAdoptanteId(Long adoptanteId);

    List<Solicitud> findByEstado(EstadoSolicitud estado);
}
