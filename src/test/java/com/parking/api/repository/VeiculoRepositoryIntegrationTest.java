package com.parking.api.repository;

import com.parking.api.model.Motorista;
import com.parking.api.model.Veiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VeiculoRepositoryIntegrationTest {

    @Autowired
    private VeiculoRepository repository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    private Motorista novoMotorista(String nome, String rg) {
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto(nome);
        motorista.setRg(rg);
        return motorista;
    }

    private Veiculo novoVeiculo(String placa, String modelo, String cor, Motorista motorista) {
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(placa);
        veiculo.setModeloCarro(modelo);
        veiculo.setCor(cor);
        veiculo.setMotorista(motorista);
        return veiculo;
    }

    @Test
    @DisplayName("Deve salvar veiculo e gerar ID")
    void deveSalvarVeiculoComSucesso() {
        Motorista motorista = motoristaRepository.save(novoMotorista("João Silva", "123456789"));
        Veiculo veiculo = novoVeiculo("ABC1D23", "Gol", "Prata", motorista);

        Veiculo salvo = repository.save(veiculo);

        assertNotNull(salvo.getId());
        assertEquals("ABC1D23", salvo.getPlaca());
        assertEquals("Gol", salvo.getModeloCarro());
        assertEquals("Prata", salvo.getCor());
    }

    @Test
    @DisplayName("Deve retornar todos os veiculos salvos")
    void deveRetornarListaCorretaDeVeiculos() {
        Motorista m1 = motoristaRepository.save(novoMotorista("Maria Santos", "987654321"));
        Motorista m2 = motoristaRepository.save(novoMotorista("Pedro Costa", "111222333"));

        Veiculo v1 = novoVeiculo("ABC1D23", "Gol", "Prata", m1);
        Veiculo v2 = novoVeiculo("XYZ9K88", "Onix", "Branco", m2);

        repository.save(v1);
        repository.save(v2);

        List<Veiculo> veiculos = repository.findAll();

        assertEquals(2, veiculos.size());
        assertTrue(veiculos.stream().anyMatch(v -> "ABC1D23".equals(v.getPlaca())));
        assertTrue(veiculos.stream().anyMatch(v -> "XYZ9K88".equals(v.getPlaca())));
    }

    @Test
    @DisplayName("Deve buscar veiculo por ID")
    void deveBuscarVeiculoPorId() {
        Motorista motorista = motoristaRepository.save(novoMotorista("Ana Lima", "444555666"));
        Veiculo salvo = repository.save(novoVeiculo("QWE4R56", "HB20", "Cinza", motorista));

        Optional<Veiculo> encontrado = repository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("QWE4R56", encontrado.get().getPlaca());
    }

    @Test
    @DisplayName("Deve atualizar dados do veiculo")
    void deveAtualizarVeiculo() {
        Motorista motorista = motoristaRepository.save(novoMotorista("Carlos Oliveira", "777888999"));
        Veiculo salvo = repository.save(novoVeiculo("JKL7M89", "Polo", "Preto", motorista));
        salvo.setCor("Vermelho");

        Veiculo atualizado = repository.save(salvo);

        assertEquals(salvo.getId(), atualizado.getId());
        assertEquals("Vermelho", atualizado.getCor());
    }

    @Test
    @DisplayName("Deve remover veiculo por ID")
    void deveRemoverVeiculo() {
        Motorista motorista = motoristaRepository.save(novoMotorista("Lucia Ferreira", "555666777"));
        Veiculo salvo = repository.save(novoVeiculo("TES1T23", "Argo", "Azul", motorista));

        repository.deleteById(salvo.getId());
        Optional<Veiculo> aposExclusao = repository.findById(salvo.getId());

        assertFalse(aposExclusao.isPresent());
    }

    @Test
    @DisplayName("Deve retornar true quando placa existir")
    void deveRetornarTrueQuandoPlacaExistir() {
        Motorista motorista = motoristaRepository.save(novoMotorista("Bruno Alves", "888999000"));
        repository.save(novoVeiculo("PLA1C23", "Corolla", "Prata", motorista));

        boolean existe = repository.existsByPlaca("PLA1C23");

        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve retornar false quando placa nao existir")
    void deveRetornarFalseQuandoPlacaNaoExistir() {
        boolean existe = repository.existsByPlaca("NAO0X00");

        assertFalse(existe);
    }
}
