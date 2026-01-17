package com.parking.api.service;

import com.parking.api.model.Ticket;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.TicketRepository;
import com.parking.api.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;


    @Autowired
    private VeiculoRepository veiculoRepository; // Use o Repository aqui

    public List<Ticket> listarTickets(){
        return repository.findAll();
    }

    public Ticket save(Ticket ticket) {
        if (ticket.getDataEntrada() == null) {
            ticket.setDataEntrada(LocalDateTime.now());
        }

        if (ticket.getVeiculo() != null && ticket.getVeiculo().getId() > 0) {
            Veiculo veiculoCompleto = veiculoRepository.findById(ticket.getVeiculo().getId())
                    .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            ticket.setVeiculo(veiculoCompleto);
        }

        return repository.save(ticket);
    }
    public Ticket update(Long id, Ticket novoTicket){
        Ticket ticketExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));

        ticketExistente.setValorPago(novoTicket.getValorPago());
        ticketExistente.setDataSaida(LocalDateTime.now());

        return  repository.save(ticketExistente);
        
    }

    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Ticket não encontrado");
        }
        repository.deleteById(id);
    }

    public Ticket checkout(Long id){
        //buscar o ticket no banco
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));
        //definir a hora de saida
        ticket.setDataSaida(LocalDateTime.now());

        //pega a dif entre a entarda e saida
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

        return  repository.save(ticket);

    }

}
