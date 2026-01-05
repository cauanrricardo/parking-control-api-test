package com.parking.api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Currency;

import java.util.List;

@Entity
@Data
public class Motorista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String nomeCompleto;

    @Column( nullable = false,unique = true)
    private String rg;

    @OneToMany(mappedBy = "motorista")
    private List<Veiculo> veiculos;


}
