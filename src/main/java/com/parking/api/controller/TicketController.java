package com.parking.api.controller;

import com.parking.api.model.Ticket;
import com.parking.api.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    @GetMapping
    public ResponseEntity<List<Ticket>> listar(){
        return ResponseEntity.ok(service.listarTickets());
    }
    @PostMapping
    public ResponseEntity<Ticket> criar(@RequestBody Ticket ticket){
        Ticket criado = service.save(ticket);
        URI location = URI.create("/api/ticket" + criado.getId());
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<Ticket> checkout(@PathVariable Long id) {
        Ticket ticket = service.checkout(id);
        return ResponseEntity.ok(ticket);
    }


    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("/calcular-valor")
    public ResponseEntity<BigDecimal> calcularValor(@RequestParam String dataEntrada, @RequestParam String dataSaida) {

        LocalDateTime entrada = LocalDateTime.parse(dataEntrada);
        LocalDateTime saida = LocalDateTime.parse(dataSaida);

        BigDecimal valor = service.calcularValor(entrada, saida);
        return ResponseEntity.ok(valor);
    }


}
