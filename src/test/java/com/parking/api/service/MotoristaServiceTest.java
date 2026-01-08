package com.parking.api.service;

import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MotoristaServiceTest {

    @Mock //cria o duble do Banco
    private MotoristaRepository repository; //simula um repositorio

    @InjectMocks //injeta o duble no service
    private MotoristaService service;

    @Test
    void naoDeveCadastrarMotoristaComRgDuplicado() {
        //given
        String rgDuplicado = "123";

        Motorista motoristaNovo = new Motorista();
        motoristaNovo.setRg(rgDuplicado);

        Motorista motoristaAntigo = new Motorista();
        motoristaAntigo.setId(1L);
        motoristaAntigo.setRg(rgDuplicado);

        //mock simula aq um rg ja cadastrado
        when(repository.findByRg(rgDuplicado)).thenReturn(Optional.of(motoristaAntigo));

        //execucao e validacao (when / then)
        //o teste espera lancar uma execao ao tentar falhar
        assertThrows(ResponseStatusException .class, () -> {
            service.salvar(motoristaNovo);
        });
    }

}
