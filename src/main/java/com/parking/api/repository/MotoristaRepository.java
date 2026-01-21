package com.parking.api.repository;

import com.parking.api.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    Optional<Motorista> findByRg(String rg);
    boolean existsByRg(String rg );
}
