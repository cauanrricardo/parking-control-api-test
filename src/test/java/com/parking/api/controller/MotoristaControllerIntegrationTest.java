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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("Deve cadastrar motorista com sucesso")
    void deveCadastrarMotoristaComSucesso() throws Exception{
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


}
