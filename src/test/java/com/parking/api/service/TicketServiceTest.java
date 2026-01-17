package com.parking.api.service;

import com.parking.api.model.Ticket;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.TicketRepository;
import com.parking.api.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private VeiculoRepository veiculoRepository;


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
    @Test
    void deveDefinirDataEntradaAutomaticamente(){
        Ticket ticket = new Ticket();
        ticket.setDataEntrada(null); //definir como  null

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer( invocation -> invocation.getArgument(0));


        Ticket Ticketsalvo = ticketService.save(ticket);

        assertNotNull(Ticketsalvo.getDataEntrada(), "A data de entrada não pode ser nula.");
    }
    @Test
    void deveManterDataEntradaQuandoJaDefinida(){
        LocalDateTime dataAntiga = LocalDateTime.of(2005,6, 21, 12, 0);

        Ticket ticket = new Ticket();
        ticket.setDataEntrada(dataAntiga);

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Ticket ticketSalvo = ticketService.save(ticket);

        assertEquals(dataAntiga, ticketSalvo.getDataEntrada(), "A data não deveria ter sido alterada");


    }
    @Test
    void deveBuscarVeiculoCompletoaoSalvar(){
        //given
        Ticket ticket = new Ticket();
        ticket.setDataEntrada(LocalDateTime.now());

        //o ticket chega so com o ID do veiculo
        Veiculo veiculoComId = new Veiculo();
        veiculoComId.setId(20L);
        ticket.setVeiculo(veiculoComId);

        //o veiculo completo que está no banco
        Veiculo veiculoCompleto = new Veiculo();
        veiculoCompleto.setId(20L);
        veiculoCompleto.setPlaca("ABC1234");
        veiculoCompleto.setCor("Rosa");

        when(veiculoRepository.findById(20L))
                .thenReturn(Optional.of(veiculoCompleto));

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ticket ticketSalvo = ticketService.save(ticket);

        assertEquals("ABC1234", ticketSalvo.getVeiculo().getPlaca());
        assertEquals("Rosa", ticketSalvo.getVeiculo().getCor());

    }

    @Test
    void deveLancarExcecaoQuandoVeiculoNaoExiste(){
        Ticket ticket = new Ticket();
        Veiculo veiculoIExistete = new Veiculo();
        veiculoIExistete.setId(99L);
        ticket.setVeiculo(veiculoIExistete);

        when(veiculoRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                ticketService.save(ticket));

        assertEquals("Veículo não encontrado", exception.getMessage());
    }
    @Test
    void deveAtualizarValorPagoEDataSaida(){
        Long id = 1L;

        Ticket ticketExistente = new Ticket();
        ticketExistente.setId(id);
        ticketExistente.setValorPago(BigDecimal.ZERO);
        ticketExistente.setDataSaida(null);

        //ticket dados novos
        Ticket ticketDadosNovos = new Ticket();
        ticketDadosNovos.setValorPago(BigDecimal.valueOf(50.0));

        //mockito
        //o service busca e encontra o ticket antigo
        when(ticketRepository.findById(id))
                .thenReturn(Optional.of(ticketExistente));

        // o service vai salvar as alterações e o answer vai devolver o objeto
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Ticket resultado = ticketService.update(id, ticketDadosNovos);

        //then
        //vericar se o valor foi atualziado pra 50
        // 0 = igual matematicamente
        assertEquals(0, BigDecimal.valueOf(50.0).compareTo(resultado.getValorPago()));
        assertNotNull(resultado.getDataSaida(), "A data de saida nao deveria ser preenchida no update");


    }
    @Test
    void deveLancarExcecaoQuandoTicketNaoExisteNoUpdate() {
        Long idInexistente = 100L;
        Ticket dadosNovos = new Ticket();

        when(ticketRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketService.update(idInexistente, dadosNovos);
        });

        assertEquals("Ticket não encontrado", exception.getMessage());


        verify(ticketRepository, never()).save(any(Ticket.class));
    }


}

