package com.parking.api.service;

import com.parking.api.exception.MotoristaNotFoundException;
import com.parking.api.exception.RgDuplicadoException;
import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Motorista salvar(Motorista motorista) {
        motorista.setId(null);

        if (motorista.getNomeCompleto() == null || motorista.getNomeCompleto().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (motorista.getNomeCompleto().trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }

        String rgPadrao = "^\\d{2}\\.\\d{3}-\\d{3}$";
        if (motorista.getRg() == null || !motorista.getRg().matches(rgPadrao)) {
            throw new IllegalArgumentException("RG deve seguir o padrão 00.000-000");
        }

        if (repository.existsByRg(motorista.getRg())) {
            throw new RgDuplicadoException(motorista.getRg());
        }

        Motorista salvo = repository.save(motorista);
        repository.flush();
        log.info("Motorista criado e persistido: id={} nome={}", salvo.getId(), salvo.getNomeCompleto());
        return salvo;
    }

    @Transactional
    public Motorista update(Long id, Motorista novoMotorista) {
        Motorista motoristaExistente = repository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException(id));

        boolean rgMudou = novoMotorista.getRg() != null && !novoMotorista.getRg().equals(motoristaExistente.getRg());
        if (rgMudou && repository.existsByRg(novoMotorista.getRg())) {
            throw new RgDuplicadoException(novoMotorista.getRg());
        }

        motoristaExistente.setNomeCompleto(novoMotorista.getNomeCompleto());
        motoristaExistente.setRg(novoMotorista.getRg());

        Motorista atualizado = repository.save(motoristaExistente);
        repository.flush();
        log.info("Motorista atualizado e persistido: id={} nome={}", atualizado.getId(), atualizado.getNomeCompleto());
        return atualizado;
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new MotoristaNotFoundException(id);
        }

        repository.deleteById(id);
        repository.flush();
        log.info("Motorista deletado e persistido: id={}", id);
    }

    @Transactional
    public Motorista buscarPorId(Long id) {
        log.debug("Buscando motorista por id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException(id));
    }
}
