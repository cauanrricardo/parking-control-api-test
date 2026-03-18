package com.parking.api.repository;

import com.parking.api.model.Ticket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TicketRepositoryIntegrationTest {

    @Autowired
    private TicketRepository repository;

    @Test
    @DisplayName("Deve salvar ticket corretamente")
    void deveSalvarTicketComSucesso(){
        Ticket ticket = new Ticket();
        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(50));

        Ticket ticketSalvo = repository.save(ticket);

        assertNotNull(ticketSalvo.getId());
        assertNotNull(ticketSalvo.getDataEntrada());

    }

    @Test
    void deveRetornarRegistroExistente(){
        Ticket ticket = new Ticket();
        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(50));

        Ticket ticketSalvo = repository.save(ticket);

        Optional<Ticket> resultado = repository.findById(ticketSalvo.getId());

        assertTrue(resultado.isPresent());
        assertEquals(ticketSalvo.getId(), resultado.get().getId());
    }

}
