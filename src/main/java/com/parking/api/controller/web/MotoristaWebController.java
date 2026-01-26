package com.parking.api.controller.web;

import com.parking.api.model.Motorista;
import com.parking.api.service.MotoristaService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motoristas")
@RequiredArgsConstructor
public class MotoristaWebController {

    private final MotoristaService service;

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @GetMapping
    public String listar(Model model, HttpServletResponse response) {
        noCache(response);
        model.addAttribute("motoristas", service.listarMotorista());
        return "motoristas/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpServletResponse response) {
        noCache(response);
        model.addAttribute("motorista", new Motorista());
        return "motoristas/forms";
    }

    @PostMapping
    public String criar(@ModelAttribute Motorista motorista, RedirectAttributes ra, Model model, HttpServletResponse response) {
        noCache(response);
        try {
            service.salvar(motorista);
            ra.addFlashAttribute("msg", "Motorista criado com sucesso!");
            return "redirect:/motoristas";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("editando", false);
            model.addAttribute("motorista", motorista);
            return "motoristas/forms";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, HttpServletResponse response) {
        noCache(response);
        Motorista motorista = service.buscarPorId(id);
        model.addAttribute("motorista", motorista);
        model.addAttribute("editando", true);
        return "motoristas/forms";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Motorista motorista, RedirectAttributes ra, HttpServletResponse response) {
        noCache(response);
        try {
            service.update(id, motorista);
            ra.addFlashAttribute("msg", "Motorista atualizado!");
            return "redirect:/motoristas";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/motoristas/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra, HttpServletResponse response) {
        noCache(response);
        try {
            service.deletar(id);
            ra.addFlashAttribute("msg", "Motorista excluido com sucesso");
            return "redirect:/motoristas";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
            return "redirect:/motoristas";
        }
    }
}
