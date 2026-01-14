package com.parking.api.service;

import com.parking.api.model.Ticket;
import com.parking.api.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void deveCobrar10ReaisParaTempoAte30Minutos(){
        //given
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        //simualr que um carro entrou há 20 minutos
        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(20));

        //mockito
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        //quando salvar retorna o propio obejto modificado pra gente conferir o valor
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Ticket ticketeSaida = ticketService.checkout(1L);

        //then
        assertEquals(BigDecimal.valueOf(10.0), ticketeSaida.getValorPago());
    }

    @Test
    void deveCobrar15ReaisParaTempoAte60Minuto(){

        //given
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        //simular o carro
        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(50));

        //mockito
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        //quando salvar, retornar o propio objeto
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer( invocation -> invocation.getArgument(0));

        //when
        Ticket ticketSaida = ticketService.checkout(1L);

        //then
        assertEquals(BigDecimal.valueOf(15.0), ticketSaida.getValorPago());

    }
    @Test
    void deveCobrar25ReaisParaTempoAte120Minutos(){
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(115));

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer( invocation -> invocation.getArgument(0));

        Ticket ticketSaida = ticketService.checkout(1L);

        assertEquals(BigDecimal.valueOf(25.0), ticketSaida.getValorPago());
    }
    @Test
    void deveCobrar30ReaisParaTempoAcimaDe120Minutos(){
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(180));

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer( invocation -> invocation.getArgument(0));

        Ticket ticketSaida = ticketService.checkout(1L);

        assertEquals(BigDecimal.valueOf(30.0), ticketSaida.getValorPago());
    }
    @Test
    void deveDefinirDataSaidaNoCheckout() {
        //given
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setDataEntrada(LocalDateTime.now().minusHours(1));
        ticket.setDataSaida(null);

        //mockito
        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        //espelho
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Ticket ticketProcessado = ticketService.checkout(1L);

        //then
        assertNotNull(ticketProcessado.getDataSaida(), "A data de saida deveria ter sido preenchida");

        //validacao
        //verifica se a data de saida é depois da de entrada
        assertTrue(ticketProcessado.getDataSaida().isAfter(ticket.getDataEntrada()));
    }
    @Test
    void deveLancarExcecaoQuandoTicketNaoExisteNoCheckout(){
        //given
       Long idInexistente = 999L;

       when(ticketRepository.findById(idInexistente))
               .thenReturn(Optional.empty()); //ta vazio, pq ja vai lancar a exceção direto

       //when e then
        assertThrows(ResponseStatusException.class, () ->{
            ticketService.checkout(idInexistente);
        });
    }

}

