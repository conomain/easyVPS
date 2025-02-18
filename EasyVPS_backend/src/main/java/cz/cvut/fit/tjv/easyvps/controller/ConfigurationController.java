package cz.cvut.fit.tjv.easyvps.controller;

import cz.cvut.fit.tjv.easyvps.controller.converter.DTOConverterInterface;
import cz.cvut.fit.tjv.easyvps.controller.dto.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps.controller.dto.ServerDTO;
import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.service.ConfigurationServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/configuration")
@AllArgsConstructor
public class ConfigurationController {

    private final ConfigurationServiceInterface configurationService;
    private final DTOConverterInterface<ConfigurationDTO, Configuration> configurationDTOConverter;

    @GetMapping
    public List<ConfigurationDTO> getConfigurations() {
        Iterable<Configuration> configurations = configurationService.readAll();
        List<ConfigurationDTO> configurationDTOS = new ArrayList<>();

        for (Configuration configuration : configurations) {
            configurationDTOS.add(configurationDTOConverter.toDTO(configuration));
        }

        return configurationDTOS;
    }

    @GetMapping("/{id}")
    public ConfigurationDTO getConfiguration(@PathVariable("id") Long id) {
        return configurationDTOConverter.toDTO(configurationService.readById(id));
    }

    @PostMapping
    public ConfigurationDTO createConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        return configurationDTOConverter.toDTO(configurationService.create(configurationDTOConverter.toEntity(configurationDTO)));
    }

    @PutMapping(path = "/{id}")
    public ConfigurationDTO updateConfiguration(@PathVariable("id") Long id,
                                                @RequestBody ConfigurationDTO configurationDTO) {
        Configuration configuration = configurationDTOConverter.toEntity(configurationDTO);
        configurationService.update(id, configuration);
        return configurationDTOConverter.toDTO(configuration);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteConfiguration(@PathVariable("id") Long id) {
        configurationService.deleteById(id);
    }
}

