package com.parking.api.controller;

import com.parking.api.model.Ticket;
import com.parking.api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ticket")
public class TicketController {

    @Autowired
    TicketService service;

    @GetMapping
    public List<Ticket> listar(){
        return service.listarTickets();
    }
    @PostMapping
    public Ticket criar(@RequestBody Ticket ticket){
        return service.save(ticket);
    }

    @PutMapping({"/{id}"})
    public Ticket update(@PathVariable Long id, @RequestBody Ticket ticket){
        return service.update(id, ticket);
    }
    @DeleteMapping({"/{id}"})
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

}
