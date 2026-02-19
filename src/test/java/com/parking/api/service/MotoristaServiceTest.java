package com.parking.api.service;

import com.parking.api.exception.MotoristaNotFoundException;
import com.parking.api.exception.RgDuplicadoException;
import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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

    @Nested
    @DisplayName("Testes do Método Salvar")
    class Salvar {

        @Test
        @DisplayName("Deve salvar motorista com sucesso")
        void deveSalvarMotoristaComSucesso() {
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Cristian Ricardo");
            motorista.setRg("00.123-456");

            // Mocok: RG não existe no banco
            when(repository.existsByRg(motorista.getRg())).thenReturn(false);

            // Mock: retorna o motorista salvo com ID
            Motorista gravadoNoBanco = new Motorista();
            gravadoNoBanco.setId(1L);
            gravadoNoBanco.setNomeCompleto(motorista.getNomeCompleto());
            gravadoNoBanco.setRg(motorista.getRg());

            when(repository.save(any(Motorista.class))).thenReturn(gravadoNoBanco);

            //when
            Motorista resultado = service.salvar(motorista);

            //then
            assertNotNull(resultado.getId());
            assertEquals(1L, resultado.getId());
            assertEquals("Cristian Ricardo", resultado.getNomeCompleto());
            verify(repository, times(1)).save(any(Motorista.class));
        }

        @Test
        @DisplayName("Não deve cadastrar motorista com RG duplicado")
        void naoDeveCadastrarMotoristaComRgDuplicado() {
            //given
            String rgDuplicado = "20.200-200";

            Motorista motoristaNovo = new Motorista();
            motoristaNovo.setNomeCompleto("Cristian Alves");
            motoristaNovo.setRg(rgDuplicado);

            // Mock: RG já existe no banco
            when(repository.existsByRg(rgDuplicado)).thenReturn(true);

            //when / then
            RgDuplicadoException exception = assertThrows(RgDuplicadoException.class, () -> {
                service.salvar(motoristaNovo);
            });

            verify(repository, never()).save(any(Motorista.class));
        }

        @Test
        @DisplayName("Não deve aceitar nome vazio")
        void naoDeveAceitarNomeVazio() {
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("");
            motorista.setRg("20.396-876");

            //when / then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(motorista);
            });

            assertEquals("Nome não pode estar vazio", exception.getMessage());
            verify(repository, never()).save(any(Motorista.class));
        }

        @Test
        @DisplayName("Não deve aceitar nome com menos de 3 caracteres")
        void naoDeveAceitarNomeComMenosDe3Caracteres() {
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Jo");
            motorista.setRg("20.396-876");

            //when / then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(motorista);
            });

            assertEquals("Nome deve ter no mínimo 3 caracteres", exception.getMessage());
            verify(repository, never()).save(any(Motorista.class));
        }

        @Test
        @DisplayName("Não deve aceitar nome nulo")
        void naoDeveAceitarNomeNulo() {
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto(null);
            motorista.setRg("20.000-000");

            //when / then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(motorista);
            });

            assertEquals("Nome não pode estar vazio", exception.getMessage());
            verify(repository, never()).save(any(Motorista.class));
        }

        @Test
        @DisplayName("Não deve aceitar RG com formato inválido (muitos caracteres)")
        void naoDeveAceitarRgComMuitosCaracteres() {
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Cristian Alves");
            motorista.setRg("20.396.780092");

            //when / then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(motorista);
            });

            assertEquals("RG deve seguir o padrão 00.000-000", exception.getMessage());
            verify(repository, never()).save(any(Motorista.class));
        }

        @Test
        @DisplayName("Não deve aceitar RG com letras")
        void naoDeveAceitarRgComLetras() {
            //given
            Motorista motorista = new Motorista();
            motorista.setNomeCompleto("Cristian Alves");
            motorista.setRg("20.A96-780");

            //when / then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(motorista);
            });

            assertEquals("RG deve seguir o padrão 00.000-000", exception.getMessage());
            verify(repository, never()).save(any(Motorista.class));
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

        @Test
        @DisplayName("Deve atualizar motorista com sucesso quando o ID existe")
        void deveAtualizarMotoristaComSucesso() {
            Long id = 1L;

            Motorista motoristaAntigo = new Motorista();
            motoristaAntigo.setId(id);
            motoristaAntigo.setNomeCompleto("Madruga");
            motoristaAntigo.setRg("101.101-009");

            Motorista motoristaAtualizado = new Motorista();
            motoristaAtualizado.setNomeCompleto("Chaves Del Ocho");
            motoristaAtualizado.setRg("888.888-888");

            when(repository.findById(id))
                    .thenReturn(Optional.of(motoristaAntigo));

            when(repository.save(any(Motorista.class)))
                    .thenAnswer(invocationOnMock ->  invocationOnMock.getArgument(0));

            Motorista resultado = service.update(id, motoristaAtualizado);

            assertNotNull(resultado);
            assertEquals("Chaves Del Ocho", resultado.getNomeCompleto());
            assertEquals("888.888-888", resultado.getRg());
            assertEquals(id, resultado.getId());

            verify(repository, times(1)).findById(id);
            verify(repository, times(1)).save(any(Motorista.class));

        }

        @Test
        @DisplayName("Deve lançar MotoristaNotFoundException ao tentar atualizar motorista inexistente")
        void naoDeveAtualizarMotoristaInexistente() {
            Long idInexistente = 99L;
            Motorista dadosParaAtualizar = new Motorista();
            dadosParaAtualizar.setNomeCompleto("Quiqo");

            when(repository.findById(idInexistente))
                    .thenReturn(Optional.empty());

            assertThrows(MotoristaNotFoundException.class, () -> {
                service.update(idInexistente, dadosParaAtualizar);
            });

            verify(repository, never()).save(any(Motorista.class));
            verify(repository, times(1)).findById(idInexistente);

        }

    }

    @Nested
    @DisplayName("Testes do Método Deletar")
    class Deletar{

        @Test
        @DisplayName("Deve deletar motorista com sucesso quando o ID existe")
        void deveDeletarMotoristaComSucesso() {
            Long id = 99L;

            when(repository.existsById(id))
                    .thenReturn(true);

            service.deletar(id);

            verify(repository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Deve lançar MotoristaNotFoundException ao tentar deletar motorista que não existe")
        void naoDeveDeletarMotoristaInexistente() {
            Long idInexistente = 99L;

            when(repository.existsById(idInexistente)).thenReturn(false);

            assertThrows(MotoristaNotFoundException.class, () -> {
                service.deletar(idInexistente);
            });

            verify(repository, never()).deleteById(anyLong());
        }

    }


}
