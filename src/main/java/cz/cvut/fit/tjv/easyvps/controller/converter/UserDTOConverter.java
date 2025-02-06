package cz.cvut.fit.tjv.easyvps.controller.converter;

import cz.cvut.fit.tjv.easyvps.controller.dto.UserDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.persistence.InstanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserDTOConverter implements DTOConverterInterface<UserDTO, User> {

    private final InstanceRepository instanceRepository;

    @Override
    public UserDTO toDTO(User user) {

        Map<Long, Long> instances = new HashMap<>();
        user.getInstances().forEach((v) -> instances.put(v.getConfiguration().getId(), v.getQuantity()));

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
            for (Long configurationId : dto.getInstances().keySet()) {

                Instance.InstanceId instanceId = new Instance.InstanceId(dto.getId(), configurationId);

                instanceRepository.findById(instanceId).ifPresent(instances::add);
            }
        }

        user.setInstances(instances);
        return user;
    }
}
