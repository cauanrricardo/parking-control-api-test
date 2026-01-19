package com.parking.api.controller;

import com.parking.api.model.Veiculo;
import com.parking.api.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/veiculo")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;

    @GetMapping
    public ResponseEntity<List<Veiculo>> listar(){
       return ResponseEntity.ok(service.listarVeiculos());
    }

    @PostMapping
    public ResponseEntity<Veiculo> criar(@RequestBody Veiculo veiculo){
        Veiculo criado = service.salvar(veiculo);
        URI location = URI.create("/api/veiculo" + criado.getId());
        return ResponseEntity.created(location).body(veiculo);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> update(@PathVariable Long id, @Valid @RequestBody Veiculo veiculo){
        Veiculo atualizado = service.update(id, veiculo);
        return  ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
