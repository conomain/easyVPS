package cz.cvut.fit.tjv.easyvps.controller.converter;

import cz.cvut.fit.tjv.easyvps.controller.dto.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.service.UserServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ConfigurationDTOConverter implements DTOConverterInterface<ConfigurationDTO, Configuration> {

    private final UserServiceInterface userService;

    @Override
    public ConfigurationDTO toDTO(Configuration configuration) {
        Set<Long> userIds = new HashSet<>();
        configuration.getInstances().forEach((v) -> userIds.add(v.getId().getUserId()));
        return new ConfigurationDTO(configuration.getId(), configuration.getName(), configuration.getCpu_cores(), configuration.getRam(),
                   configuration.getStorage(), configuration.getPrice(), userIds);
    }

    @Override
    public Configuration toEntity(ConfigurationDTO dto) {
        Configuration configuration = new Configuration();
        configuration.setId(dto.getId());
        configuration.setName(dto.getName());
        configuration.setCpu_cores(dto.getCpu_cores());
        configuration.setRam(dto.getRam());
        configuration.setStorage(dto.getStorage());
        configuration.setPrice(dto.getPrice());

        List<Instance> instances = new ArrayList<>();
        if (dto.getUserIds() != null) {
            for (Long userId : dto.getUserIds()) {

                User user = userService.readById(userId);
                if (user == null) {
                    continue;
                }

                user.getInstances().stream()
                        .filter(instance -> Objects.equals(instance.getConfiguration().getId(), dto.getId()))
                        .forEach(instances::add);
            }
        }

        configuration.setInstances(instances);
        return configuration;
    }
}
