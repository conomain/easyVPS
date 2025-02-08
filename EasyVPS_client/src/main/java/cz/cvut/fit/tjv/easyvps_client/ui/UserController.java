package cz.cvut.fit.tjv.easyvps_client.ui;

import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.UserDTO;
import cz.cvut.fit.tjv.easyvps_client.service.ConfigurationService;
import cz.cvut.fit.tjv.easyvps_client.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private ConfigurationService configurationService;

    @GetMapping
    public String showAllUsers(Model model) {
        List<UserDTO> users = userService.readAll();
        model.addAttribute("users", users);
        return "user_list";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        Optional<UserDTO> userOpt = userService.read(id);
        if (userOpt.isPresent()) {
            UserDTO user = userOpt.get();
            model.addAttribute("user", user);

            List<InstanceDTO> instances = userService.getUserInstances(id);
            model.addAttribute("instances", instances);

            Map<Long, ConfigurationDTO> configurations = new HashMap<>();
            for (InstanceDTO instance : instances) {
                Long configId = instance.getConfigurationId();
                if (!configurations.containsKey(configId)) {
                    Optional<ConfigurationDTO> configOpt = userService.findConfigurationById(instance.getConfigurationId());
                    configOpt.ifPresent(configurationDTO -> configurations.put(configId, configurationDTO));
                }
            }
            model.addAttribute("configurations", configurations);

            return "user_details";
        }
        return "redirect:/user";
    }

    @GetMapping("/{id}/instances")
    public String showUserInstances(@PathVariable Long id, Model model) {
        List<InstanceDTO> instances = userService.getUserInstances(id);
        model.addAttribute("instances", instances);
        return "instance_list";
    }

    @GetMapping("/{id}/instances/{configId}")
    public String showUserInstance(@PathVariable Long id, @PathVariable Long configId,
                                   @RequestParam("hash") String ipHash, Model model) {
        Optional<InstanceDTO> instanceOpt = userService.getUserInstance(id, configId, ipHash);
        if (instanceOpt.isPresent()) {
            InstanceDTO instance = instanceOpt.get();
            model.addAttribute("instance", instance);

            Optional<ConfigurationDTO> configurationOpt = userService.findConfigurationById(instance.getConfigurationId());
            configurationOpt.ifPresent(configurationDTO -> model.addAttribute("configuration", configurationDTO));
            return "instance_details";
        }

        return "redirect:/user/" + id;
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user_form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute UserDTO user, RedirectAttributes redirectAttributes) {
        try {
            userService.create(user);
        } catch (HttpClientErrorException.BadRequest e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/user";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        Optional<UserDTO> user = userService.read(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user_form";
        }
        return "redirect:/user";
    }

    @PostMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, @ModelAttribute UserDTO user, RedirectAttributes redirectAttributes) {
        try {
            userService.update(user);
        } catch (HttpClientErrorException.BadRequest e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/user";
    }

    @GetMapping("/{id}/instances/create")
    public String showCreateInstanceForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        List<ConfigurationDTO> configurations = configurationService.readAll();
        model.addAttribute("configurations", configurations);
        return "instance_create_form";
    }

    @PostMapping("/{id}/instances/create")
    public String createInstance(@PathVariable Long id,
                                 @RequestParam("configurationId") Long configurationId,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.addInstanceToUser(id, configurationId);
        }
        catch (HttpClientErrorException.BadRequest e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/user/" + id;
    }

    @PostMapping("/{id}/start")
    public String startInstance(@PathVariable Long id, @RequestParam Long configurationId) {
        userService.addInstanceToUser(id, configurationId);
        return "redirect:/user/" + id + "/instances";
    }

    @PostMapping("/{id}/stop")
    public String stopInstance(@PathVariable Long id, @RequestParam Long configurationId,
                               @RequestParam("hash") String ipHash) {
        userService.removeInstanceFromUser(id, configurationId, ipHash);
        return "redirect:/user/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
        } catch (HttpClientErrorException.BadRequest e) {
            redirectAttributes.addFlashAttribute("error", "User deletion failed: " + e.getMessage());
        }
        return "redirect:/user";
    }
}
