package cz.cvut.fit.tjv.easyvps_client.ui;

import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import org.springframework.ui.Model;

import cz.cvut.fit.tjv.easyvps_client.model.ServerDTO;
import cz.cvut.fit.tjv.easyvps_client.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/server")
@AllArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    public String showAllServers(Model model) {
        List<ServerDTO> servers = serverService.readAll();
        model.addAttribute("servers", servers);
        return "server_list";
    }

    @GetMapping("/{id}")
    public String showServerDetails(@PathVariable Long id, Model model) {
        Optional<ServerDTO> server = serverService.read(id);

        if (server.isPresent()) {
            ServerDTO serverDTO = server.get();
            List<InstanceDTO> instances = serverService.getServerInstances(id);

            model.addAttribute("server", serverDTO);
            model.addAttribute("instances", instances);

            return "server_details";
        }

        return "redirect:/server";
    }

    @GetMapping("/create")
    public String showCreateServerForm(Model model) {
        model.addAttribute("server", new ServerDTO());
        return "server_form";
    }

    @PostMapping("/create")
    public String createServer(@ModelAttribute ServerDTO serverDTO, RedirectAttributes redirectAttributes) {
        try {
            serverService.createServer(serverDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Server creation failed: " + e.getMessage());
        }
        return "redirect:/server";
    }

    @GetMapping("/{id}/edit")
    public String showEditServerForm(@PathVariable Long id, Model model) {
        Optional<ServerDTO> server = serverService.read(id);
        if (server.isPresent()) {
            model.addAttribute("server", server.get());
            return "server_form";
        }
        return "redirect:/server";
    }

    @PostMapping("/{id}/edit")
    public String editServer(@PathVariable Long id, @ModelAttribute ServerDTO serverDTO, RedirectAttributes redirectAttributes) {
        try {
            serverService.updateServer(serverDTO);
            redirectAttributes.addFlashAttribute("success", "Server updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Server update failed: " + e.getMessage());
        }
        return "redirect:/server";
    }

    @GetMapping("/{id}/delete")
    public String deleteServer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            serverService.deleteServer(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Server deletion failed: " + e.getMessage());
        }
        return "redirect:/server";
    }
}
