package com.parking.api.repository;

import com.parking.api.model.Ticket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    void deveRetornarRegistroInexistente(){
        Optional<Ticket> resultadoInexistente = repository.findById(99L);

        assertFalse(resultadoInexistente.isPresent());

    }

    @Test
    void deveRetornarListaCorretaDetickets(){
    Ticket t1 = new Ticket();
    Ticket t2 = new Ticket();

    t1.setDataEntrada(LocalDateTime.now().minusMinutes(30));
    t2.setDataEntrada(LocalDateTime.now().minusMinutes(20));

    repository.save(t1);
    repository.save(t2);

    List<Ticket> tickets = repository.findAll();

    assertEquals(2, tickets.size());

    }

    @Test
    void deveRetornarTrueSeExisteId(){
        Ticket ticket = new Ticket();
        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(30));

        Ticket salvo = repository.save(ticket);

        Optional<Ticket> idExistente = repository.findById(salvo.getId());

        assertTrue(idExistente.isPresent());
        assertEquals(salvo.getId(), idExistente.get().getId());
    }

    @Test @DisplayName("Deve retornar vazio ao buscar id inexistente")
    void deveRetornarVazioQuandoIdNaoExiste() {
        Optional<Ticket> encontrado = repository.findById(99L);
        assertTrue(encontrado.isEmpty());
    }

}
