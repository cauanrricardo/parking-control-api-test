package com.parking.api.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true) //regras pro db, nao pode ir vazio e nem duplciado
    private String placa;

    private String modeloCarro;

    private String cor;

    @ManyToOne
    @JoinColumn(name = "motorista_id", nullable = false)
   private Motorista motorista;

    @OneToMany
    private List<Parking> tickets;

}