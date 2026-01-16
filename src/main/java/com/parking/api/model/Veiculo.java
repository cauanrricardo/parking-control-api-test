package com.parking.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true) //regras pro db, nao pode ir vazio e nem duplciado
    @NotBlank(message = "A placa é obrigatória")
    @Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres")
    private String placa;

    @NotBlank(message = "O modelo é obrigatório")
    private String modeloCarro;

    @NotBlank(message = "Cor é obrigatória")
    private String cor;

    @ManyToOne
    @JoinColumn(name = "motorista_id", nullable = false)
   private Motorista motorista;

    @OneToMany
    private List<Ticket> tickets;

}