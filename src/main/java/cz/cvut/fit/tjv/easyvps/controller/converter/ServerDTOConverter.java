package cz.cvut.fit.tjv.easyvps.controller.converter;

import cz.cvut.fit.tjv.easyvps.controller.dto.ServerDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.persistence.InstanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class ServerDTOConverter implements DTOConverterInterface<ServerDTO, Server> {

    private final InstanceRepository instanceRepository;

    @Override
    public ServerDTO toDTO(Server server) {
        Map<Long, Long> instances = new HashMap<>();
        server.getInstances().forEach((v) -> instances.put(v.getConfiguration().getId(), v.getQuantity()));
        return new ServerDTO(server.getId(), server.getCpu_cores(), server.getRam(), server.getStorage(), instances);
    }

    @Override
    public Server toEntity(ServerDTO dto) {
        Server server = new Server();
        server.setId(dto.getId());
        server.setCpu_cores(dto.getCpu_cores());
        server.setRam(dto.getRam());
        server.setStorage(dto.getStorage());

        Set<Instance> instances = new HashSet<>();
        if (dto.getInstances() != null) {
            for (Long configurationId : dto.getInstances().keySet()) {

                Instance.InstanceId instanceId = new Instance.InstanceId(dto.getId(), configurationId);

                instanceRepository.findById(instanceId).ifPresent(instances::add);
            }
        }

        server.setInstances(instances);
        return server;
    }
}
