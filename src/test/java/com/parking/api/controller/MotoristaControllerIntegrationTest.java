package com.parking.api.controller;

import com.parking.api.model.Motorista;
import com.parking.api.repository.MotoristaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // contexto completo da aplicação Spring Boot para o teste
@AutoConfigureMockMvc //configurar automaticamente o mockmvc
@ActiveProfiles("test")
public class MotoristaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // eh a ferramenta que simula chamadas HTTP no teste

    @Autowired
    private ObjectMapper objectMapper;  // converte json - objeto java e ao contrario

    @Autowired
    private MotoristaRepository repository;

    @BeforeEach
    void limparBanco(){
        repository.deleteAll();
    }

    @Test
    @DisplayName(" POST -> Deve cadastrar motorista com sucesso")
    void deveCadastrarMotoristaComSucesso() throws Exception{ //throws exception nesse caso eh usado pra se alguma coisa aconetcer, pode deixar subir
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo");
        motorista.setRg("11.111-897");

        mockMvc.perform(post("/api/motorista") //execute uma chamada HTTP simulada
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motorista))) // transforma o objeto motorista em texto JSON e coloca no corpo da requisição
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nomeCompleto").value("Cauan Ricardo"))
                .andExpect(jsonPath("$.rg").value("11.111-897"));
    }

    @Test
    void deveListarTodosOsMotoristas() throws Exception{
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo");
        motorista.setRg("11.111-897");

        Motorista motorista2 = new Motorista();
        motorista2.setNomeCompleto("Ricardo Ribeiro");
        motorista2.setRg("00.000-897");

        repository.save(motorista);
        repository.save(motorista2);

        mockMvc.perform(get("/api/motorista"))
                .andExpect(status().isOk()) //status 200
                .andExpect(jsonPath("$").isArray())//validar se voltou um array
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeCompleto").value("Cauan Ricardo"))
                .andExpect(jsonPath("$[1].nomeCompleto").value("Ricardo Ribeiro"));
    }

    @Test
    void deveListarMotoristapeloId() throws Exception{
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo");
        motorista.setRg("11.111-897");

        Motorista salvo = repository.save(motorista);

        mockMvc.perform(get("/api/motorista/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nomeCompleto").value("Cauan Ricardo"));
    }

    @Test
    void naoDeveListarMotoristaPeloIdinexistente() throws Exception {
        mockMvc.perform(get("/api/motorista/{id}", 99L))
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("Deve atualizar o motorista")
    void deveAtualizarMotorista() throws  Exception{
           /*
    * Given → salva no banco
      When → faz PUT com novos dados
      Then → valida que foi atualizado*/

        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo");
        motorista.setRg("11.111-897");

        Motorista salvo = repository.save(motorista);

        Motorista motoristaAtualizado = new Motorista();
        motoristaAtualizado.setNomeCompleto("Ricardo Ribeiro");
        motoristaAtualizado.setRg("11.111-800");

        mockMvc.perform(put("/api/motorista/{id}", salvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(motoristaAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value(motoristaAtualizado.getNomeCompleto()))
                .andExpect(jsonPath("$.rg").value(motoristaAtualizado.getRg()));
    }

    @Test
    @DisplayName("DELETE -> Deletando um motorista")
    void deletarMotorista() throws Exception{
        Motorista motorista = new Motorista();
        motorista.setNomeCompleto("Cauan Ricardo");
        motorista.setRg("11.111-897");

        Motorista salvo = repository.save(motorista);

        mockMvc.perform(delete("/api/motorista/{id}", salvo.getId()))
                .andExpect(status().isNoContent());
    }



}
