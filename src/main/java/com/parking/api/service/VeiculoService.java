package com.parking.api.service;

import com.parking.api.model.Motorista;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.MotoristaRepository;
import com.parking.api.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository repository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    public List<Veiculo> listarVeiculos(){
        return  repository.findAll();
    }

    public Veiculo salvar(Veiculo veiculo){
        //validacao motorista existe
        if(veiculo.getMotorista() == null || veiculo.getMotorista().getId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O veículo precisa de um motorista com ID valido");
        }
        //validacao da placa
        String placaPadrao = "^[A-Z]{3}\\d{4}$";
        if(!veiculo.getPlaca().matches(placaPadrao)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Placa deve seguir o padrão AAA0000");
        }
        if(repository.existsByPlaca(veiculo.getPlaca())){
            throw new RuntimeException("Já existe um veículo cadastrado com esta placa!");
        }

        Motorista motoristaCompleto = motoristaRepository.findById(veiculo.getMotorista().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Motorista não encontrado"));

        // 3. Vincula o motorista "cheio" de dados ao veículo
        veiculo.setMotorista(motoristaCompleto);
        return repository.save(veiculo);
    }
    public Veiculo update(Long id, Veiculo novoVeiculo){
        Veiculo veiculoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veiculo não encontardo"));

        veiculoExistente.setPlaca(novoVeiculo.getPlaca());
        veiculoExistente.setCor(novoVeiculo.getCor());
        veiculoExistente.setModeloCarro(novoVeiculo.getModeloCarro());

        return repository.save(veiculoExistente);
    }

    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motorista não encontrado");
        }
        repository.deleteById(id);
    }

}
