package com.parking.api.service;

import com.parking.api.exception.MotoristaNotFoundException;
import com.parking.api.exception.PlacaDuplicadaException;
import com.parking.api.exception.VeiculoNotFoundException;
import com.parking.api.model.Motorista;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.MotoristaRepository;
import com.parking.api.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VeiculoService {


    private final VeiculoRepository repository;
    private final MotoristaRepository motoristaRepository;

    public List<Veiculo> listarVeiculos(){
        log.debug("Listando veículos");
        return  repository.findAll();
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo){
        log.info("Criando veículo: placa={}", veiculo.getPlaca());

        //validacao motorista existe
        if(veiculo.getMotorista() == null || veiculo.getMotorista().getId() == null){
            log.warn("Tentativa de criar veículo sem motorista válido");
            throw new IllegalArgumentException("O veículo precisa de um motorista com ID válido");
        }

        //validacao da placa
        String placaPadrao = "^[A-Z]{3}\\d{4}$";
        if(!veiculo.getPlaca().matches(placaPadrao)){
            log.warn("Placa inválida: {}", veiculo.getPlaca());
            throw new IllegalArgumentException("Placa deve seguir o padrão AAA0000");
        }

        if(repository.existsByPlaca(veiculo.getPlaca())){
            log.warn("Placa já cadastrada: {}", veiculo.getPlaca());
            throw new PlacaDuplicadaException(veiculo.getPlaca());
        }

        Long idMotorista = veiculo.getMotorista().getId();
        Motorista motoristaCompleto = motoristaRepository.findById(veiculo.getMotorista().getId())
                .orElseThrow(() -> {
                    log.warn("Motorista não encontrado: id={}", veiculo.getMotorista().getId());
                    return new MotoristaNotFoundException(idMotorista);
                });

        // vincula o motorista "cheio" de dados ao veículo
        veiculo.setMotorista(motoristaCompleto);

        Veiculo salvo = repository.save(veiculo);
        log.info("Veículo criado: id={} placa={}", salvo.getId(), salvo.getPlaca());
        return  salvo;
    }

    @Transactional
    public Veiculo update(Long id, Veiculo novoVeiculo){
        log.info("Atualizando veículo: id={}", id);

        Veiculo veiculoExistente = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Veículo não encontrado para update: id={}", id);
                    return new VeiculoNotFoundException(id);
                });

        veiculoExistente.setPlaca(novoVeiculo.getPlaca());
        veiculoExistente.setCor(novoVeiculo.getCor());
        veiculoExistente.setModeloCarro(novoVeiculo.getModeloCarro());

        Veiculo atualizado =  repository.save(veiculoExistente);
        log.info("Veículo atualizado: id={} placa={}", atualizado.getId(), atualizado.getPlaca());
        return  atualizado;
    }

    @Transactional
    public void deletar(Long id){
        log.info("Deletando veículo: id={}", id);

        if(!repository.existsById(id)){
            log.warn("Veículo não encontrado para delete: id={}", id);
            throw new VeiculoNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Veículo deletado: id={}", id);
    }

}
