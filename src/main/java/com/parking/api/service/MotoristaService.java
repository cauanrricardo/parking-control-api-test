package com.parking.api.service;

import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository repository;

    public List<Motorista> listarMotorista(){
        return  repository.findAll();
    }

    public Motorista salvar(Motorista motorista){
        repository.findByRg(motorista.getRg()).ifPresent(motorista1 -> {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "RG já cadastrado");
        });
        return  repository.save(motorista);
    }

    public Motorista update(Long id, Motorista novoMotorista){
        Motorista motoristaExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));

        motoristaExistente.setNomeCompleto(novoMotorista.getNomeCompleto());
        motoristaExistente.setRg(novoMotorista.getRg());

        return  repository.save(motoristaExistente);
    }
    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    repository.deleteById(id);
    }
}
