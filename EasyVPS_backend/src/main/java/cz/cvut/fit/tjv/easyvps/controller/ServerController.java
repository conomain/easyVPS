package cz.cvut.fit.tjv.easyvps.controller;

import cz.cvut.fit.tjv.easyvps.controller.converter.DTOConverterInterface;
import cz.cvut.fit.tjv.easyvps.controller.dto.InstanceDTO;
import cz.cvut.fit.tjv.easyvps.controller.dto.ServerDTO;
import cz.cvut.fit.tjv.easyvps.controller.dto.UserDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.service.ServerServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/server")
@AllArgsConstructor
public class ServerController {

    private ServerServiceInterface serverService;
    private final DTOConverterInterface<ServerDTO, Server> serverDTOConverter;
    private final DTOConverterInterface<InstanceDTO, Instance> instanceDTOConverter;

    @GetMapping
    public Set<ServerDTO> getServers() {
        Iterable<Server> servers = serverService.readAll();
        Set<ServerDTO> serverDTOS = new HashSet<>();

        for (Server server : servers) {
            serverDTOS.add(serverDTOConverter.toDTO(server));
        }

        return serverDTOS;
    }

    @GetMapping("/{id}")
    public ServerDTO getServer(@PathVariable("id") Long id) {
        return serverDTOConverter.toDTO(serverService.readById(id).get());
    }

    @GetMapping("/{id}/instances")
    public Set<InstanceDTO> getServerInstances(@PathVariable("id") Long id) {
        Server server = serverService.readById(id).get();
        Set<InstanceDTO> instanceDTOS = new HashSet<>();

        for (Instance instance : server.getInstances()) {
            instanceDTOS.add(instanceDTOConverter.toDTO(instance));
        }

        return instanceDTOS;
    }

    @PostMapping
    public ServerDTO createServer(@RequestBody ServerDTO serverDTO) {
        return serverDTOConverter.toDTO(serverService.create(serverDTOConverter.toEntity(serverDTO)));
    }

    @PutMapping(path = "/{id}")
    public ServerDTO updateServer(@PathVariable("id") Long id,
                                  @RequestBody ServerDTO serverDTO) {
        Server server = serverDTOConverter.toEntity(serverDTO);
        serverService.update(id, server);
        return serverDTOConverter.toDTO(server);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteServer(@PathVariable("id") Long id) {
        serverService.deleteById(id);
    }
}
