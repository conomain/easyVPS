package cz.cvut.fit.tjv.easyvps_client.ui;

import org.springframework.ui.Model;

import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps_client.service.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/configuration")
@AllArgsConstructor
public class ConfigurationController {

    private ConfigurationService configurationService;

    @GetMapping
    public String showAllConfigurations(Model model) {
        List<ConfigurationDTO> configurations = configurationService.readAll();
        model.addAttribute("configurations", configurations);
        return "configuration_list";
    }

    @GetMapping("/{id}")
    public String showConfigurationDetails(@PathVariable Long id, Model model) {
        Optional<ConfigurationDTO> configuration = configurationService.read(id);
        if (configuration.isPresent()) {
            model.addAttribute("configuration", configuration.get());
            return "configuration_details";
        }
        return "redirect:/configuration";
    }

    @GetMapping("/create")
    public String showCreateConfigurationForm(Model model) {
        model.addAttribute("configuration", new ConfigurationDTO());
        return "configuration_form";
    }

    @PostMapping("/create")
    public String createConfiguration(@ModelAttribute ConfigurationDTO configurationDTO, RedirectAttributes redirectAttributes) {
        try {
            configurationService.create(configurationDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Configuration creation failed: " + e.getMessage());
        }
        return "redirect:/configuration";
    }

    @GetMapping("{id}/edit")
    public String showEditConfigurationForm(@PathVariable Long id, Model model) {
        Optional<ConfigurationDTO> configuration = configurationService.read(id);
        if (configuration.isPresent()) {
            model.addAttribute("configuration", configuration.get());
            return "configuration_form";
        }
        return "redirect:/configuration";
    }

    @PostMapping("{id}/edit")
    public String editConfiguration(@PathVariable Long id, @ModelAttribute ConfigurationDTO configuration, RedirectAttributes redirectAttributes) {
        try {
            configurationService.update(configuration);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Configuration update failed: " + e.getMessage());
        }
        return "redirect:/configuration";
    }

    @GetMapping("{id}/delete")
    public String deleteConfiguration(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            configurationService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Configuration deletion failed: " + e.getMessage());
        }
        return "redirect:/configuration";
    }
}
