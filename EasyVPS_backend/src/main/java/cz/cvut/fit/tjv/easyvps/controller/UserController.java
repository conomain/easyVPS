package cz.cvut.fit.tjv.easyvps.controller;

import cz.cvut.fit.tjv.easyvps.controller.converter.DTOConverterInterface;
import cz.cvut.fit.tjv.easyvps.controller.dto.InstanceDTO;
import cz.cvut.fit.tjv.easyvps.controller.dto.UserDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.service.UserServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Update user by ID", description = "Updates a user's details based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public UserDTO updateUser(@PathVariable("id") Long id,
                              @RequestBody UserDTO userDTO) {
        User user = userDTOConverter.toEntity(userDTO);
        userService.update(id, user);
        return userDTOConverter.toDTO(user);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }

    @PostMapping(path = "/{id}/start")
    @Operation(summary = "Add an instance to a user", description = "Adds a new instance to a user with the specified configuration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instance added successfully"),
            @ApiResponse(responseCode = "404", description = "User or configuration not found")
    })
    public UserDTO addInstanceToUser(@PathVariable("id") Long userId,
                                  @RequestParam("configurationId") Long configurationId) {
        return userDTOConverter.toDTO(userService.addInstanceToUser(configurationId, userId));
    }

    @DeleteMapping(path = "/{id}/stop")
    @Operation(summary = "Remove an instance from a user", description = "Removes an instance from a user by configuration ID and IP hash.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instance removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or configuration not found")
    })
    public UserDTO removeInstanceFromUser(@PathVariable("id") Long userId,
                                       @RequestParam("configurationId") Long configurationId,
                                       @RequestParam("hash") String ipHash) {
        return userDTOConverter.toDTO(userService.removeInstanceFromUser(userId, configurationId, ipHash));
    }
}
