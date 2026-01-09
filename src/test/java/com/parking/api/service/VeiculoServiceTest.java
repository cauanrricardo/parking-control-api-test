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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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


}
