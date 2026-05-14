package com.example.demo_basic.repository;

import com.example.demo_basic.model.entity.Adoptante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptanteRepository extends JpaRepository<Adoptante, Long> {
}
