package cz.cvut.fit.tjv.easyvps.controller;

import cz.cvut.fit.tjv.easyvps.controller.converter.DTOConverterInterface;
import cz.cvut.fit.tjv.easyvps.controller.dto.InstanceDTO;
import cz.cvut.fit.tjv.easyvps.controller.dto.UserDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.service.UserServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserServiceInterface userService;
    private final DTOConverterInterface<UserDTO, User> userDTOConverter;
    private final DTOConverterInterface<InstanceDTO, Instance> instanceDTOConverter;

    @GetMapping
    public Set<UserDTO> getUsers() {
        Iterable<User> users = userService.readAll();
        Set<UserDTO> userDTOS = new HashSet<>();

        for (User user : users) {
            userDTOS.add(userDTOConverter.toDTO(user));
        }

        return userDTOS;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return userDTOConverter.toDTO(userService.readById(id).get());
    }

    @GetMapping("/{id}/instances")
    public Set<InstanceDTO> getUserInstances(@PathVariable("id") Long id) {
        User user = userService.readById(id).get();
        Set<InstanceDTO> instanceDTOS = new HashSet<>();

        for (Instance instance : user.getInstances()) {
            instanceDTOS.add(instanceDTOConverter.toDTO(instance));
        }

        return instanceDTOS;
    }

    @GetMapping(path = "/{id}/instances/{configurationId}")
    public InstanceDTO getInstance(@PathVariable("id") Long userId,
                                @PathVariable("configurationId") Long configurationId,
                                @RequestParam("hash") String ipHash) {
        return instanceDTOConverter.toDTO(userService.findInstance(userId, configurationId, ipHash));
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userDTOConverter.toDTO(userService.create(userDTOConverter.toEntity(userDTO)));
    }

    @PutMapping(path = "/{id}")
    public UserDTO updateUser(@PathVariable("id") Long id,
                              @RequestBody UserDTO userDTO) {
        User user = userDTOConverter.toEntity(userDTO);
        userService.update(id, user);
        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }

    @PostMapping(path = "/{id}/start")
    public UserDTO addInstanceToUser(@PathVariable("id") Long userId,
                                  @RequestParam("configurationId") Long configurationId) {
        return userDTOConverter.toDTO(userService.addInstanceToUser(configurationId, userId));
    }

    @DeleteMapping(path = "/{id}/stop")
    public UserDTO removeInstanceFromUser(@PathVariable("id") Long userId,
                                       @RequestParam("configurationId") Long configurationId,
                                       @RequestParam("hash") String ipHash) {
        return userDTOConverter.toDTO(userService.removeInstanceFromUser(userId, configurationId, ipHash));
    }
}
