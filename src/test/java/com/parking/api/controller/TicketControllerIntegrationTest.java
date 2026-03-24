package com.parking.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.api.model.Ticket;
import com.parking.api.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository repository;

    @BeforeEach
    void limpar(){
        repository.deleteAll();
    }

    @Test
    void criarTicketComSucesso() throws Exception{
        Ticket ticket = new Ticket();
        ticket.setDataEntrada(LocalDateTime.now().minusMinutes(20));

        mockMvc.perform(post("/api/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dataEntrada").isNotEmpty());

    }

}
