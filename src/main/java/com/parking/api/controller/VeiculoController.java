package com.parking.api.controller;

import com.parking.api.model.Veiculo;
import com.parking.api.repository.VeiculoRepository;
import com.parking.api.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculo")
public class VeiculoController {


    @Autowired
    private VeiculoService service;

    @GetMapping
    public List<Veiculo> listar(){
       return service.listarVeiculos();
    }

    @PostMapping
    public Veiculo criar(@RequestBody Veiculo veiculo){
        return  service.salvar(veiculo);
    }

    @PutMapping("/{id}")
    public Veiculo update(@PathVariable Long id, @Valid @RequestBody Veiculo veiculo){
        return service.update(id, veiculo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        service.deletar(id);
    }

}
