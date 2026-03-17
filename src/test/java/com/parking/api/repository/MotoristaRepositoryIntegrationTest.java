package com.parking.api.repository;

import com.parking.api.model.Motorista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.resolver.MockParameterResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest // quem injeta o repository eh o Spring
public class MotoristaRepositoryIntegrationTest {

    @Autowired //tem q usar  essa injencao em vez do construtor
    private MotoristaRepository repository;

    @Test
    @DisplayName("deve salvar o motorista com sucesso")
    void deveSalvarMotoristasComSucesso(){
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo Ribeiro");
        motorista.setRg("20.333-875");

        Motorista salvo = repository.save(motorista);

        assertNotNull(salvo.getId());
        assertEquals("Cauan Ricardo Ribeiro", salvo.getNomeCompleto());
        assertEquals("20.333-875", salvo.getRg());

    }

    @Test
    @DisplayName("deve retornar true se o rg ja existe")
    void deveRetornarTrueseRgJaExiste(){
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo Ribeiro");
        motorista.setRg("20.333-875");

        repository.save(motorista);

        boolean existeRg = repository.existsByRg("20.333-875");

        assertTrue(existeRg);
    }

    @Test
    @DisplayName("deve retornar false se o rg nao existe")
    void deveRetornarFalseRgInexistente(){
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo Ribeiro");
        motorista.setRg("20.333-875");

        repository.save(motorista);

        boolean existeRg = repository.existsByRg("11.111-111");

        assertFalse(existeRg);
    }

    @Test
    @DisplayName("deve buscar motorista por ID com sucesso")
    void deveBuscarPorId(){
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo Ribeiro");
        motorista.setRg("20.333-875");

        Motorista salvo = repository.save(motorista);

        Optional<Motorista> resultado = repository.findById(salvo.getId());

        assertTrue(resultado.isPresent());
        assertEquals("20.333-875", salvo.getRg());

    }

    @Test
    @DisplayName("Deve retornar vazio quando motorista não existe")
    void deveRetornarVazioQuandoNaoExiste() {
        Optional<Motorista> resultado = repository.findById(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve listar motoristas ordenados por nome")
    void deveListarMotoristasOrdenadosPorNome() {
        Motorista m1 = new Motorista();
        m1.setNomeCompleto("Bruno Silva");
        m1.setRg("11.111-111");

        Motorista m2 = new Motorista();
        m2.setNomeCompleto("Ana Souza");
        m2.setRg("22.222-222");

        repository.save(m1);
        repository.save(m2);

        List<Motorista> motoristas = repository.findAll(Sort.by("nomeCompleto"));

        assertEquals(2, motoristas.size());
        assertEquals("Ana Souza", motoristas.get(0).getNomeCompleto());
        assertEquals("Bruno Silva", motoristas.get(1).getNomeCompleto());

    }
}
