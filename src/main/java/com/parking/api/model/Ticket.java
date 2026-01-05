package com.parking.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private BigDecimal valorPago;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;


}
