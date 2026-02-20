package daw2.mariomontes.mensajeria.controllers;

import daw2.mariomontes.mensajeria.entities.Envio;
import daw2.mariomontes.mensajeria.repositories.EnvioRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/envios")
public class EnvioController {

    private static final Logger logger = LoggerFactory.getLogger(EnvioController.class);

    @Autowired
    private EnvioRepository envioRepository;

    @GetMapping
    public String listEnvios(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(required = false) String search,
                             Model model) {

        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("id").ascending());
        Page<Envio> envios;

        if (search != null && !search.isBlank()) {
            envios = envioRepository.findByDescripcionContainingIgnoreCase(search, pageable);
        } else {
            envios = envioRepository.findAll(pageable);
        }

        model.addAttribute("listEnvios", envios.toList());
        model.addAttribute("totalPages", envios.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);

        return "envios";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("envio", new Envio());
        return "envio-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Envio> envioOpt = envioRepository.findById(id);
        if (envioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se encontró el envío con ID " + id);
            return "redirect:/envios";
        }
        model.addAttribute("envio", envioOpt.get());
        return "envio-form";
    }

    @PostMapping("/insert")
    public String insertEnvio(@Valid @ModelAttribute("envio") Envio envio,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "envio-form";
        }
        envioRepository.save(envio);
        redirectAttributes.addFlashAttribute("successMessage", "Envío creado correctamente");
        return "redirect:/envios";
    }

    @PostMapping("/update")
    public String updateEnvio(@Valid @ModelAttribute("envio") Envio envio,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "envio-form";
        }
        envioRepository.save(envio);
        redirectAttributes.addFlashAttribute("successMessage", "Envío actualizado correctamente");
        return "redirect:/envios";
    }

    @PostMapping("/delete")
    public String deleteEnvio(@RequestParam("id") Long id,
                              RedirectAttributes redirectAttributes) {
        envioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Envío eliminado correctamente");
        return "redirect:/envios";
    }
}