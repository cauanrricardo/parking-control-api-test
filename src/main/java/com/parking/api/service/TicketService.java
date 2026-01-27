// src/main/java/com/parking/api/service/TicketService.java
package com.parking.api.service;

import com.parking.api.exception.TicketNotFoundException;
import com.parking.api.exception.VeiculoNotFoundException;
import com.parking.api.model.Ticket;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.TicketRepository;
import com.parking.api.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository repository;
    private final VeiculoRepository veiculoRepository;

    public List<Ticket> listarTickets() {
        log.debug("Listando tickets");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        log.info("Criando ticket");

        if (ticket.getDataEntrada() == null) {
            ticket.setDataEntrada(LocalDateTime.now());
        }

        if (ticket.getVeiculo() != null && ticket.getVeiculo().getId() != null && ticket.getVeiculo().getId() > 0) {
            Long veiculoId = ticket.getVeiculo().getId();
            log.debug("Carregando veiculoId={} para associar no ticket", veiculoId);

            Veiculo veiculoCompleto = veiculoRepository.findById(veiculoId)
                    .orElseThrow(() -> {
                        log.warn("Falha ao criar ticket: Veículo {} não encontrado", veiculoId);
                        return new VeiculoNotFoundException(veiculoId);
                    });

            ticket.setVeiculo(veiculoCompleto);
        }

        Ticket salvo = repository.save(ticket);
        repository.flush();
        log.info("Ticket criado e persistido: id={}", salvo.getId());
        return salvo;
    }

    @Transactional
    public Ticket update(Long id, Ticket novoTicket) {
        log.info("Atualizando ticket: id={}", id);

        Ticket ticketExistente = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ticket não encontrado para update: id={}", id);
                    return new TicketNotFoundException(id);
                });

        ticketExistente.setValorPago(novoTicket.getValorPago());
        ticketExistente.setDataSaida(LocalDateTime.now());

        Ticket atualizado = repository.save(ticketExistente);
        repository.flush();
        log.info("Ticket atualizado e persistido: id={}", atualizado.getId());
        return atualizado;
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando ticket: id={}", id);

        if (!repository.existsById(id)) {
            log.warn("Ticket não encontrado para delete: id={}", id);
            throw new TicketNotFoundException(id);
        }

        repository.deleteById(id);
        repository.flush();
        log.info("Ticket deletado e persistido: id={}", id);
    }

    @Transactional
    public Ticket checkout(Long id) {
        log.info("Checkout do ticket: id={}", id);

        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ticket não encontrado para checkout: id={}", id);
                    return new TicketNotFoundException(id);
                });

        ticket.setDataSaida(LocalDateTime.now());

        long minutos = java.time.Duration.between(ticket.getDataEntrada(), ticket.getDataSaida()).toMinutes();

        BigDecimal valor;
        if (minutos <= 30) {
            valor = BigDecimal.valueOf(10.0);
        } else if (minutos <= 60) {
            valor = BigDecimal.valueOf(15.0);
        } else if (minutos <= 120) {
            valor = BigDecimal.valueOf(25.0);
        } else {
            valor = BigDecimal.valueOf(30.0);
        }

        ticket.setValorPago(valor);

        Ticket finalizado = repository.save(ticket);
        repository.flush();
        log.info("Checkout finalizado: id={} minutos={} valorPago={}", finalizado.getId(), minutos, valor);
        return finalizado;
    }

    @Transactional
    public BigDecimal calcularValor(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        log.debug("Calculando valor para período: {} até {}", dataEntrada, dataSaida);

        if (dataEntrada == null || dataSaida == null) {
            return BigDecimal.ZERO;
        }

        long minutos = java.time.Duration.between(dataEntrada, dataSaida).toMinutes();

        BigDecimal valor;
        if (minutos <= 30) {
            valor = BigDecimal.valueOf(10.0);
        } else if (minutos <= 60) {
            valor = BigDecimal.valueOf(15.0);
        } else if (minutos <= 120) {
            valor = BigDecimal.valueOf(25.0);
        } else {
            valor = BigDecimal.valueOf(30.0);
        }

        log.debug("Valor calculado: {} para {} minutos", valor, minutos);
        return valor;
    }

}
