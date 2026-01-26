package com.parking.api.controller.web;

import com.parking.api.model.Veiculo;
import com.parking.api.service.VeiculoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/veiculos")
public class VeiculoWebController {

    private final VeiculoService veiculoService;

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @GetMapping
    public String listarVeiculos(Model model, HttpServletResponse response) {
        noCache(response);
        model.addAttribute("veiculos", veiculoService.listarVeiculos());
        return "veiculos/lista";
    }

    @GetMapping("/novo")
    public String novoVeiculo(Model model, HttpServletResponse response) {
        noCache(response);
        model.addAttribute("veiculo", new Veiculo());
        return "veiculos/forms";
    }

    @PostMapping
    public String criarVeiculo(@ModelAttribute Veiculo veiculo, RedirectAttributes ra, Model model, HttpServletResponse response) {
        noCache(response);
        try {
            veiculoService.salvar(veiculo);
            ra.addFlashAttribute("msg", "Veiculo criado com sucesso!");
            return "redirect:/veiculos";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("editando", false);
            model.addAttribute("veiculo", veiculo);
            return "veiculos/forms";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, HttpServletResponse response) {
        noCache(response);
        Veiculo veiculo = veiculoService.buscarPorId(id);
        model.addAttribute("veiculo", veiculo);
        model.addAttribute("editando", true);
        return "veiculos/forms";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Veiculo veiculo, RedirectAttributes ra, HttpServletResponse response) {
        noCache(response);
        try {
            veiculoService.update(id, veiculo);
            ra.addFlashAttribute("msg", "Veiculo atualizado com sucesso");
            return "redirect:/veiculos";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/veiculos/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra, HttpServletResponse response) {
        noCache(response);
        try {
            veiculoService.deletar(id);
            ra.addFlashAttribute("msg", "Veiculo excluido com sucesso");
            return "redirect:/veiculos";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
            return "redirect:/veiculos";
        }
    }
}
