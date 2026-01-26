// src/main/java/com/parking/api/controller/web/TicketWebController.java
package com.parking.api.controller.web;

import com.parking.api.model.Ticket;
import com.parking.api.service.TicketService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketWebController {

    private final TicketService ticketService;

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @GetMapping
    public String listarTickets(Model model, HttpServletResponse response) {
        noCache(response);
        model.addAttribute("tickets", ticketService.listarTickets());
        return "tickets/lista";
    }

    @GetMapping("/novo")
    public String novoTicket(Model model, HttpServletResponse response) {
        noCache(response);
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("editando", false);
        return "tickets/forms";
    }

    @PostMapping
    public String criarTicket(@ModelAttribute Ticket ticket,
                              RedirectAttributes ra,
                              Model model,
                              HttpServletResponse response) {
        noCache(response);
        try {
            ticketService.save(ticket);
            ra.addFlashAttribute("msg", "Ticket criado com sucesso!");
            return "redirect:/tickets";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("editando", false);
            model.addAttribute("ticket", ticket);
            return "tickets/forms";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, HttpServletResponse response) {
        noCache(response);
        Ticket ticket = ticketService.listarTickets().stream()
                .filter(t -> t.getId() != null && t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado: " + id));

        model.addAttribute("ticket", ticket);
        model.addAttribute("editando", true);
        return "tickets/forms";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute Ticket ticket,
                            RedirectAttributes ra,
                            HttpServletResponse response) {
        noCache(response);
        try {
            ticketService.update(id, ticket);
            ra.addFlashAttribute("msg", "Ticket atualizado com sucesso");
            return "redirect:/tickets";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/tickets/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra, HttpServletResponse response) {
        noCache(response);
        try {
            ticketService.deletar(id);
            ra.addFlashAttribute("msg", "Ticket excluido com sucesso");
            return "redirect:/tickets";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
            return "redirect:/tickets";
        }
    }
}
