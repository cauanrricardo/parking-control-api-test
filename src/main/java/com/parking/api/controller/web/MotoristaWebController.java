package com.parking.api.controller.web;

import com.parking.api.model.Motorista;
import com.parking.api.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motoristas")
@RequiredArgsConstructor
public class MotoristaWebController {

    private final MotoristaService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("motoristas", service.listarMotorista());
        return "motoristas/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("motorista", new Motorista());
        return "motoristas/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Motorista motorista) {
        service.salvar(motorista);
        return "redirect:/motoristas";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Motorista motorista = service.listarMotorista().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow();
        model.addAttribute("motorista", motorista);
        return "motoristas/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Motorista motorista) {
        service.update(id, motorista);
        return "redirect:/motoristas";
    }

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id) {
        service.deletar(id);
        return "redirect:/motoristas";
    }
}
