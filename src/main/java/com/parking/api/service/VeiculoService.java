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
import org.springframework.data.domain.Sort;
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

    public List<Veiculo> listarVeiculos() {
        log.debug("Listando veiculos");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo) {
        veiculo.setId(null);

        if (veiculo.getPlaca() == null || veiculo.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Placa nao pode estar vazia");
        }

        String placa = veiculo.getPlaca().trim().toUpperCase();
        veiculo.setPlaca(placa);

        String placaPadrao = "^[A-Z]{3}\\d{4}$";
        if (!placa.matches(placaPadrao)) {
            throw new IllegalArgumentException("Placa deve seguir o padrao AAA0000");
        }

        if (repository.existsByPlaca(placa)) {
            throw new PlacaDuplicadaException(placa);
        }

        if (veiculo.getMotorista() == null || veiculo.getMotorista().getId() == null) {
            throw new IllegalArgumentException("O veiculo precisa de um motorista com ID valido");
        }

        Long idMotorista = veiculo.getMotorista().getId();
        Motorista motoristaCompleto = motoristaRepository.findById(idMotorista)
                .orElseThrow(() -> new MotoristaNotFoundException(idMotorista));

        veiculo.setMotorista(motoristaCompleto);

        Veiculo salvo = repository.save(veiculo);
        repository.flush();
        log.info("Veiculo criado e persistido: id={} placa={}", salvo.getId(), salvo.getPlaca());
        return salvo;
    }

    @Transactional
    public Veiculo update(Long id, Veiculo novoVeiculo) {
        Veiculo veiculoExistente = repository.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id));

        if (novoVeiculo.getPlaca() == null || novoVeiculo.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Placa nao pode estar vazia");
        }

        String placaNova = novoVeiculo.getPlaca().trim().toUpperCase();

        String placaPadrao = "^[A-Z]{3}\\d{4}$";
        if (!placaNova.matches(placaPadrao)) {
            throw new IllegalArgumentException("Placa deve seguir o padrao AAA0000");
        }

        boolean placaMudou = veiculoExistente.getPlaca() == null || !placaNova.equals(veiculoExistente.getPlaca());
        if (placaMudou && repository.existsByPlaca(placaNova)) {
            throw new PlacaDuplicadaException(placaNova);
        }

        veiculoExistente.setPlaca(placaNova);
        veiculoExistente.setCor(novoVeiculo.getCor());
        veiculoExistente.setModeloCarro(novoVeiculo.getModeloCarro());

        if (novoVeiculo.getMotorista() != null && novoVeiculo.getMotorista().getId() != null) {
            Long idMotorista = novoVeiculo.getMotorista().getId();
            Motorista motoristaCompleto = motoristaRepository.findById(idMotorista)
                    .orElseThrow(() -> new MotoristaNotFoundException(idMotorista));
            veiculoExistente.setMotorista(motoristaCompleto);
        }

        Veiculo atualizado = repository.save(veiculoExistente);
        repository.flush();
        log.info("Veiculo atualizado e persistido: id={} placa={}", atualizado.getId(), atualizado.getPlaca());
        return atualizado;
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new VeiculoNotFoundException(id);
        }

        repository.deleteById(id);
        repository.flush();
        log.info("Veiculo deletado e persistido: id={}", id);
    }

    @Transactional
    public Veiculo buscarPorId(Long id) {
        log.debug("Buscando veiculo por id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id));
    }
}
