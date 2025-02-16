package cz.cvut.fit.tjv.easyvps.controller.converter;

import cz.cvut.fit.tjv.easyvps.controller.dto.ServerDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.persistence.InstanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ServerDTOConverter implements DTOConverterInterface<ServerDTO, Server> {

    private final InstanceRepository instanceRepository;

    @Override
    public ServerDTO toDTO(Server server) {
        Map<String, Long> instances = new HashMap<>();
        server.getInstances().forEach((v) -> instances.put(v.getId().getIpHash(), v.getConfiguration().getId()));
        return new ServerDTO(server.getId(), server.getCpu_cores(), server.getRam(), server.getStorage(), instances);
    }

    @Override
    public Server toEntity(ServerDTO dto) {
        Server server = new Server();
        server.setId(dto.getId());
        server.setCpu_cores(dto.getCpu_cores());
        server.setRam(dto.getRam());
        server.setStorage(dto.getStorage());

        List<Instance> instances = new ArrayList<>();
        if (dto.getInstances() != null) {
            for (Map.Entry<String, Long> entry : dto.getInstances().entrySet()) {
                Long configurationId = entry.getValue();
                String ipHash = entry.getKey();

                Instance.InstanceId instanceId = new Instance.InstanceId(dto.getId(), configurationId, ipHash);
                Optional<Instance> instance = instanceRepository.findById(instanceId);
                instance.ifPresent(instances::add);
            }
        }

        server.setInstances(instances);
        return server;
    }
}
