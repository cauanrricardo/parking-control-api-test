package com.parking.api.controller;

import com.parking.api.model.Motorista;
import com.parking.api.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/motorista")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService service;

    @GetMapping
    public ResponseEntity<List<Motorista>> listar(){
        return ResponseEntity.ok(service.listarMotorista());
    }

    @PostMapping
    public ResponseEntity<Motorista> criar( @RequestBody Motorista motorista){
        Motorista motoristaCriado = service.salvar(motorista);
        URI location = URI.create("/api/motorista/" + motoristaCriado.getId());
        return  ResponseEntity.created(location).body(motoristaCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Motorista> update(@PathVariable Long id, @Valid @RequestBody Motorista motorista){
        Motorista motoristaAtualizado = service.update(id, motorista);
        return  ResponseEntity.ok(motoristaAtualizado);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deletar( @PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
