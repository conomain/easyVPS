package cz.cvut.fit.tjv.easyvps.controller.converter;

import cz.cvut.fit.tjv.easyvps.controller.dto.UserDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.persistence.InstanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class UserDTOConverter implements DTOConverterInterface<UserDTO, User> {

    private final InstanceRepository instanceRepository;

    @Override
    public UserDTO toDTO(User user) {

        Map<String, Long> instances = new HashMap<>();
        user.getInstances().forEach((v) -> instances.put(v.getId().getIpHash(), v.getConfiguration().getId()));

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), instances);
    }

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        Set<Instance> instances = new HashSet<>();
        if (dto.getInstances() != null) {
            for (Map.Entry<String, Long> entry : dto.getInstances().entrySet()) {
                Long configurationId = entry.getValue();
                String ipHash = entry.getKey();

                Instance.InstanceId instanceId = new Instance.InstanceId(dto.getId(), configurationId, ipHash);
                Optional<Instance> instance = instanceRepository.findById(instanceId);
                instance.ifPresent(instances::add);
            }
        }

        user.setInstances(instances);
        return user;
    }
}
