package com.parking.api.controller;

import com.parking.api.model.Motorista;
import com.parking.api.service.MotoristaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motorista")
public class MotoristaController {

    @Autowired
    private MotoristaService service;

    @GetMapping
    public List<Motorista> listar(){
        return service.listarMotorista();
    }

    @PostMapping
    public Motorista criar( @RequestBody Motorista motorista){
        return service.salvar(motorista);
    }

    @PutMapping("/{id}")
    public Motorista update(@PathVariable Long id, @Valid @RequestBody Motorista motorista){
        return service.update(id, motorista);
    }

    @DeleteMapping("/{id}")
    public  void deletar( @PathVariable Long id){
    service.deletar(id);
    }


}
