package com.parking.api.repository;

import com.parking.api.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByPlaca(String placa); //faz uma consulta sql pra veriifcar se ja existe a palca
}
