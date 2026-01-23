package com.parking.api.service;

import com.parking.api.exception.MotoristaNotFoundException;
import com.parking.api.exception.RgDuplicadoException;
import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MotoristaService {

    private final MotoristaRepository repository;

    public List<Motorista> listarMotorista() {
        log.debug("Listando motoristas");
        return repository.findAll();
    }

    @Transactional
    public Motorista salvar(Motorista motorista) {
        motorista.setId(null);

        log.info("Criando motorista: nome={}", motorista.getNomeCompleto());

        //validacao nome
        if (motorista.getNomeCompleto() == null || motorista.getNomeCompleto().isEmpty()) {
            log.warn("Tentativa de criar motorista com nome vazio");
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (motorista.getNomeCompleto().trim().length() < 3) {
            log.warn("Nome inválido (muito curto): {}", motorista.getNomeCompleto());
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }

        //validacao rg
        String rgPadrao = "^\\d{2}\\.\\d{3}-\\d{3}$";
        if (motorista.getRg() == null || !motorista.getRg().matches(rgPadrao)) {
            log.warn("RG inválido: {}", motorista.getRg());
            throw new IllegalArgumentException("RG deve seguir o padrão 00.000-000");
        }

        if (repository.existsByRg(motorista.getRg())) {
            log.warn("RG já cadastrado: {}", motorista.getRg());
            throw new RgDuplicadoException(motorista.getRg());
        }

        return repository.save(motorista);
    }

    @Transactional
    public Motorista update(Long id, Motorista novoMotorista) {
        log.info("Atualizando motorista: id={}", id);

        Motorista motoristaExistente = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Motorista não encontrado para update: id={}", id);
                    return new MotoristaNotFoundException(id);
                });

        motoristaExistente.setNomeCompleto(novoMotorista.getNomeCompleto());
        motoristaExistente.setRg(novoMotorista.getRg());

        if (!motoristaExistente.getRg().equals(novoMotorista.getRg())
                && repository.existsByRg(novoMotorista.getRg())) {
            throw new RgDuplicadoException(novoMotorista.getRg());
        }


        Motorista atualizado = repository.save(motoristaExistente);
        log.info("Motorista atualizado: id={} nome={}", atualizado.getId(), atualizado.getNomeCompleto());
        return atualizado;
    }

    public void deletar(Long id) {
        log.info("Deletando motorista: id={}", id);

        if (!repository.existsById(id)) {
            throw new MotoristaNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Motorista deletado: id={}", id);
    }

    @Transactional
    public Motorista buscarPorId(Long id) {
        log.debug("Buscando motorista por id: {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Motorista não encontrado: id={}", id);
                    return new MotoristaNotFoundException(id);
                });
    }
}
