package com.parking.api.service;

import com.parking.api.model.Motorista;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.MotoristaRepository;
import com.parking.api.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VeiculoServiceTest {

    @Mock
    private VeiculoRepository repository;

    @Mock
    private MotoristaRepository motoristaRepository;

    @InjectMocks
    private VeiculoService service;

    @Test
    void naoDeveSalvarVeiculoSeMotoristaNaoExistir(){
        //given
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("RIO25T7");

        Motorista motoristaFake = new Motorista();
        motoristaFake.setId(99L); // ID que não existe no banco
        veiculo.setMotorista(motoristaFake);
        veiculo.setMotorista(motoristaFake);

        when(motoristaRepository.findById(99L)).thenReturn(Optional.empty());

        //when / then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.salvar(veiculo);
        });

        //validacao
        assertEquals("Motorista não encontrado", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
    @Test
    void deveSalvarVeiculoComSucesso(){
        //given
        Motorista motoristaExistente = new Motorista();
        motoristaExistente.setId(1L);
        motoristaExistente.setNomeCompleto("Ana Silva");
        motoristaExistente.setRg("12.345-678");

        //cena 01
        Veiculo veiculoEntrada = new Veiculo();
        veiculoEntrada.setPlaca("RIO25T7");
        veiculoEntrada.setMotorista(motoristaExistente);

        //fingindo que o motorista existe no banco
        when(motoristaRepository.findById(1L))
                .thenReturn(Optional.of(motoristaExistente));

        //cena 02
        Veiculo veiculoSalvo = new Veiculo();
        veiculoSalvo.setId(10L);
        veiculoSalvo.setPlaca("RIO25T7");
        veiculoSalvo.setMotorista(motoristaExistente);

        when(repository.save(any(Veiculo.class)))
                .thenReturn(veiculoSalvo);

        //when
        Veiculo resultado = service.salvar(veiculoEntrada);

        //then
        assertNotNull(resultado.getId());
        assertEquals("RIO25T7", resultado.getPlaca());
        assertEquals("Ana Silva", resultado.getMotorista().getNomeCompleto());
    }

    @Test
    void naoDeveSalvarVeiculoComPlacaInvalida(){
      //given
        Motorista motoristaExistente = new Motorista();
        motoristaExistente.setNomeCompleto("Julia Silva");
        motoristaExistente.setId(1L);

        Veiculo veiculo  = new Veiculo();
        veiculo.setPlaca("INVALIDA1");
        veiculo.setMotorista(motoristaExistente);

        //when / then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.salvar(veiculo);
        });

        assertEquals("Placa deve seguir o padrão AAA0000", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    }

    @Test
    void naoDeveAceitarPlacaComLetrasMinusculas(){
        //given
        Motorista motorista = new Motorista();
        motorista.setId(1L);

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("abc1234");
        veiculo.setMotorista(motorista);

        //when / then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.salvar(veiculo);
        });

        assertEquals("Placa deve seguir o padrão AAA0000", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    }

    @Test
    void naoDeveSalvarVeiculoComPlacaDuplicada(){
        //given
        Motorista motorista = new Motorista();
        motorista.setId(1L);
        motorista.setNomeCompleto("Cauan Ricardo");

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("ABC1234");
        veiculo.setMotorista(motorista);

        when(repository.existsByPlaca("ABC1234")).thenReturn(true); //"Quando alguém chamar repository.existsByPlaca("ABC1234"), retorne true"

        //when / then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.salvar(veiculo);
        });

        //validacao
        assertEquals("Ja existe um veiculo cadastrado com esta placa!", exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }



}
