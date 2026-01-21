package com.parking.api.service;

import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MotoristaServiceTest {

    @Mock //cria o duble do Banco
    private MotoristaRepository repository; //simula um repositorio

    @InjectMocks //injeta o duble no service
    private MotoristaService service;

    // ====== TESTES SALVAR ========
    @Nested
    @DisplayName("Testes do Método Salvar")
    class Salvar{

        @Test
        void deveSalvarMotoristaComSucesso(){
            //given (O que o usuário envia pelo Postman)
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Cristian Ricardo");
            motorista.setRg("00.123-456");

            //dublê - diga que o rg esta livre
            when(repository.findByRg(motorista.getRg())).thenReturn(Optional.empty());

            // criamos o objeto que REPRESENTA o dado gravado no banco (com ID)
            Motorista gravadoNoBanco = new Motorista();
            gravadoNoBanco.setId(1L);
            gravadoNoBanco.setNomeCompleto("Cauan Silva");
            gravadoNoBanco.setRg("20.396-476");

            // Mock: Quando o service te mandar salvar,  devolve o motorista com um ID
            when(repository.save(org.mockito.ArgumentMatchers.any(Motorista.class))).thenReturn(gravadoNoBanco);

            //when
            Motorista resultado = service.salvar(motorista);

            //assert
            org.junit.jupiter.api.Assertions.assertNotNull(resultado.getId()); //verifica se o id existe
            assertEquals(1L, resultado.getId()); //verifica se o id é igual a 1
            assertEquals("Cauan Silva", resultado.getNomeCompleto()); //verifica se o nome é igual

        }


        @Test
        @DisplayName("Não deve cadastrar motorista com RG duplicado")
        void naoDeveCadastrarMotoristaComRgDuplicado() {
            //given
            String rgDuplicado = "20.200-200";

            Motorista motoristaNovo = new Motorista();
            motoristaNovo.setNomeCompleto("Cristian Alves");
            motoristaNovo.setRg(rgDuplicado);

            Motorista motoristaAntigo = new Motorista();
            motoristaAntigo.setId(1L);
            motoristaAntigo.setRg(rgDuplicado);

            //mock simula aq um rg ja cadastrado
            when(repository.findByRg(rgDuplicado)).thenReturn(Optional.of(motoristaAntigo));

            //execucao e validacao (when / then)
            //o teste espera lancar uma execao ao tentar falhar
            ResponseStatusException exception = assertThrows(ResponseStatusException .class, () -> {
                service.salvar(motoristaNovo);
            });
            assertEquals("RG já cadastrado", exception.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }

        @Test
        @DisplayName("Não deve aceitar nome vazio")
        void naoDeveAceitarNomeVazio(){
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("");
            motorista.setRg("20.396-876");

            //when / then
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                service.salvar(motorista);
            });
            //validacao
            assertEquals("Nome não pode estar vazio", exception.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        }

        @Test
        @DisplayName("Não deve aceitar nome com menos de 3 caracteres")
        void naoDeveAceitarNomeComMenosDe3Caracteres(){
            //given (dado q eu tenho um motorista com nome curto
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Jo");
            motorista.setRg("20.396-876");

            //when e then (quando salvar espero que falhe)
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                service.salvar(motorista);
            });
            assertEquals("Nome deve ter no minimo 3 caracteres", exception.getReason()); //mensagem da excecao
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        }

        @Test
        @DisplayName("Não deve aceitar nome nulo")
        void naoDeveAceitarNomeNulo(){
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto(null);
            motorista.setRg("20.000-000");

            //when / then
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                service.salvar(motorista);
            });

            //validacao
            assertEquals("Nome não pode estar vazio", exception.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        }

        @Test
        @DisplayName("Não deve aceitar nome com menos de 3 caracteres")
        void naoDeveAceitarRgComMuitosCaracteres(){
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Crstian Alves");
            motorista.setRg("20.396.780092");

            //when/then
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                service.salvar(motorista);
            });

            assertEquals("RG deve seguir o padrão 00.000-000", exception.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }

        @Test
        @DisplayName("Não deve aceitar RG com letras")
        void naoDeveAceitarRgComLetras(){
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Crstian Alves");
            motorista.setRg("20.A96-780");

            //when/then
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                service.salvar(motorista);
            });

            //validacao
            assertEquals("RG deve seguir o padrão 00.000-000", exception.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }

    }

    @Nested
    @DisplayName("Testes do Método Listar")
    class Listar{

        @Test
        @DisplayName("Deve listar todos os motoristas")
        void deveListarTodosOsMotoristas(){

            Motorista motorista1 = new Motorista();
            motorista1.setNomeCompleto("Cauan Ricardo");
            motorista1.setRg("20.458-99");
            motorista1.setId(1L);

            Motorista motorista2 = new Motorista();
            motorista2.setNomeCompleto("Ricardo Silva");
            motorista2.setRg("00.008-99");
            motorista2.setId(2L);

            List<Motorista> listaMotoristas = List.of(motorista1, motorista2);

            when(repository.findAll())
                    .thenReturn(listaMotoristas);

            List<Motorista> resultado = service.listarMotorista();

            assertNotNull(listaMotoristas);
            assertEquals(2, resultado.size());
            assertEquals("Cauan Ricardo", resultado.get(0).getNomeCompleto());
            assertEquals("Ricardo Silva", resultado.get(1).getNomeCompleto());

            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há motoristas")
        void deveRetornarListaVaziaQuandoNaoHaMotoristas(){
         List<Motorista> listaVazia = List.of();

        when(repository.findAll())
            .thenReturn(listaVazia);

        List<Motorista> resultado = service.listarMotorista();

        assertEquals(0, resultado.size());

        verify(repository, never()).save(any(Motorista.class));
        }


    }

    @Nested
    @DisplayName("Testes do Método Atualizar")
    class Atualizar{


    }

    @Nested
    @DisplayName("Testes do Método Deletar")
    class Deletar{


    }


}
