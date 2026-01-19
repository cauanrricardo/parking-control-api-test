package com.parking.api.service;

import com.parking.api.exception.MotoristaNotFoundException;
import com.parking.api.exception.PlacaDuplicadaException;
import com.parking.api.exception.VeiculoNotFoundException;
import com.parking.api.model.Motorista;
import com.parking.api.model.Veiculo;
import com.parking.api.repository.MotoristaRepository;
import com.parking.api.repository.VeiculoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VeiculoServiceTest {

    @Mock
    private VeiculoRepository repository;

    @Mock
    private MotoristaRepository motoristaRepository;

    @InjectMocks
    private VeiculoService service;

    // ====== TESTES SALVAR ========
    @Nested
    @DisplayName("Testes do Método Salvar")
    class Salvar{

        @Test
        void deveSalvarVeiculoComSucesso(){
            //given
            Motorista motoristaExistente = new Motorista();
            motoristaExistente.setId(1L);
            motoristaExistente.setNomeCompleto("Ana Silva");
            motoristaExistente.setRg("12.345-678");

            //cena 01
            Veiculo veiculoEntrada = new Veiculo();
            veiculoEntrada.setPlaca("CCC1234");
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
        void naoDeveSalvarVeiculoSeMotoristaNaoExistir(){
            //given
            Veiculo veiculo = new Veiculo();
            veiculo.setPlaca("ABC1234");

            Motorista motoristaFake = new Motorista();
            motoristaFake.setId(99L); // ID que não existe no banco
            veiculo.setMotorista(motoristaFake);

            when(motoristaRepository.findById(99L)).thenReturn(Optional.empty());

            //when / then
            MotoristaNotFoundException exception = assertThrows(MotoristaNotFoundException.class, () -> {
                service.salvar(veiculo);
            });

            //validacao
            assertEquals("Motorista não encontrado com o ID: 99", exception.getMessage());
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
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(veiculo);
            });

            assertEquals("Placa deve seguir o padrão AAA0000", exception.getMessage());


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
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                service.salvar(veiculo);
            });

            assertEquals("Placa deve seguir o padrão AAA0000", exception.getMessage());

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

            when(repository.existsByPlaca("ABC1234"))
                    .thenReturn(true); //quando alguém chamar repository.existsByPlaca("ABC1234"), retorne true"

            //when / then
            PlacaDuplicadaException exception = assertThrows(PlacaDuplicadaException.class, () -> {
                service.salvar(veiculo);
            });

            //validacao
            assertEquals("Ja existe um veiculo cadastrado com esta placa: ABC1234", exception.getMessage());

        }

    }
    @Nested
    @DisplayName("Testes do Método Listar")
    class Listar {
        @Test
        void deveListarTodosOsVeiculos(){

            Veiculo v1 = new Veiculo();
            Veiculo v2 = new Veiculo();

            List<Veiculo> listaVeiculos = List.of(v1, v2);

            when(repository.findAll())
                    .thenReturn(listaVeiculos);

            List<Veiculo> resultado = service.listarVeiculos(); // o resultado vai receber a listaveiculos retornada do listarVeiuclos();

            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            verify(repository, times(1)).findAll();
        }

        @Test
        void deveRetornarListaVaziaQuandoNaoHaVeiculos(){
            List<Veiculo> listaVeiculos = List.of();

            when(repository.findAll())
                    .thenReturn(listaVeiculos);

            List<Veiculo> resultado = service.listarVeiculos();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty(), " A lista deve estar vazia");
            assertEquals(0, resultado.size());


        }


    }

    @Nested
    @DisplayName("Testes do Método Atualizar")
    class Atualizar {
        @Test
      void deveAtualizarVeiculoComSucesso() {
            Long id = 1L;

            Veiculo veiculo  = new Veiculo();
            veiculo.setId(id);
            veiculo.setPlaca("ABC1234");
            veiculo.setCor("Rosa");
            veiculo.setModeloCarro("Civic");

            Veiculo veiculoAtualizado = new Veiculo();
            veiculoAtualizado.setModeloCarro("Civic v8");
            veiculoAtualizado.setPlaca("CAC1234");
            veiculoAtualizado.setCor("Preto");

            when(repository.findById(id))
                    .thenReturn(Optional.of(veiculo));

            //o save retorna o objeto atualizado
            when(repository.save(any(Veiculo.class)))
                    .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

            Veiculo resultado = service.update(id, veiculoAtualizado);

            assertNotNull(resultado);
            assertEquals("CAC1234", resultado.getPlaca());
            assertEquals("Preto", resultado.getCor());
            assertEquals("Civic v8", resultado.getModeloCarro());

            //verifica se o id nao mudou
            assertEquals(id, resultado.getId());

            verify(repository, times(1)).findById(id);
            verify(repository, times(1)).save(any(Veiculo.class));

        }

        @Test
        void naoDeveAtualizarVeiculoInexistente(){
            Long idInexistente = 999L;

            Veiculo veiculoNovo = new Veiculo();
            veiculoNovo.setPlaca("ABC1234");

            when(repository.findById(idInexistente))
                    .thenReturn(Optional.empty());

         VeiculoNotFoundException exception =   assertThrows(VeiculoNotFoundException.class, () -> {
                 service.update(idInexistente, veiculoNovo);
            });

            assertEquals("Veículo não encontrado com o ID: 999", exception.getMessage());
            verify(repository, never()).save(any(Veiculo.class));
            verify(repository, times(1)).findById(idInexistente);
        }

    }

    @Nested
    @DisplayName("Testes do Método Deletar")
    class Deletar {
        @Test
        void deveDeletarVeiculoComSucesso(){
            Long id = 1L;

            when(repository.existsById(id))
                    .thenReturn(true);

            service.deletar(id);

            verify(repository, times(1)).deleteById(id);
        }

        @Test
        void naoDeveDeletarVeiculoInexistente(){
            Long idInexistnte = 999L;

            when(repository.existsById(idInexistnte))
                    .thenReturn(false);


          VeiculoNotFoundException exception =   assertThrows(VeiculoNotFoundException.class, () -> {
                service.deletar(idInexistnte);
            });

            assertEquals("Veículo não encontrado com o ID: 999", exception.getMessage());
            verify(repository, never()).deleteById(anyLong());
        }

    }


}
