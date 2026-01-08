package com.parking.api.service;

import com.parking.api.model.Ticket;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.TicketRepository;
import com.parking.api.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            // Agora o findById vai funcionar porque estamos usando o Repository
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

}
