package cz.cvut.fit.tjv.easyvps.cotroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.easyvps.controller.UserController;
import cz.cvut.fit.tjv.easyvps.controller.dto.InstanceDTO;
import cz.cvut.fit.tjv.easyvps.controller.dto.UserDTO;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.service.UserServiceInterface;
import cz.cvut.fit.tjv.easyvps.controller.converter.DTOConverterInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserServiceInterface userService;

    @MockitoBean
    private DTOConverterInterface<UserDTO, User> userDTOConverter;

    @MockitoBean
    private DTOConverterInterface<InstanceDTO, Instance> instanceDTOConverter;


    @Test
    public void testCreateUser() throws Exception {
        UserDTO inputDTO = new UserDTO(null, "NewUser", "newuser@example.com", "password123", Collections.emptyMap());

        User userEntity = new User();
        userEntity.setUsername("NewUser");
        userEntity.setEmail("newuser@example.com");
        userEntity.setPassword("password123");

        User createdUser = new User();
        createdUser.setId(100L);
        createdUser.setUsername("NewUser");
        createdUser.setEmail("newuser@example.com");
        createdUser.setPassword("password123");

        UserDTO outputDTO = new UserDTO(100L, "NewUser", "newuser@example.com", "password123", Collections.emptyMap());

        when(userDTOConverter.toEntity(any(UserDTO.class))).thenReturn(userEntity);
        when(userService.create(userEntity)).thenReturn(createdUser);
        when(userDTOConverter.toDTO(createdUser)).thenReturn(outputDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJSON = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.username").value("NewUser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("pass1");

        UserDTO userDTO = new UserDTO(1L, "user1", "user1@example.com", "pass1", Collections.emptyMap());

        Set<User> users = new HashSet<>();
        users.add(user);

        when(userService.readAll()).thenReturn(users);
        when(userDTOConverter.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"));
    }



    @Test
    public void testGetOneUserById() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("secret");

        UserDTO userDTO = new UserDTO(userId, "user1", "user1@example.com", "secret", Collections.emptyMap());

        when(userService.readById(userId)).thenReturn(Optional.of(user));
        when(userDTOConverter.toDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    public void testGetUserInstances() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("secret");

        Instance instance = new Instance();
        Instance.InstanceId instanceId = new Instance.InstanceId(userId, 10L, "hash");
        instance.setId(instanceId);
        instance.setIp("192.168.1.100");
        user.getInstances().add(instance);

        InstanceDTO instanceDTO = new InstanceDTO(10L, userId, 5L, "192.168.1.100", "hash");

        when(userService.readById(userId)).thenReturn(Optional.of(user));
        when(instanceDTOConverter.toDTO(instance)).thenReturn(instanceDTO);

        mockMvc.perform(get("/api/user/{id}/instances", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].configurationId").value(10L))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].serverId").value(5L))
                .andExpect(jsonPath("$[0].ip").value("192.168.1.100"))
                .andExpect(jsonPath("$[0].ipHash").value("hash"));
    }

    @Test
    public void testGetInstance() throws Exception {
        Long userId = 1L;
        Long configurationId = 10L;
        String ipHash = "hash";

        Instance instance = new Instance();
        Instance.InstanceId instanceId = new Instance.InstanceId(userId, configurationId, ipHash);
        instance.setId(instanceId);
        instance.setIp("192.168.1.100");

        InstanceDTO instanceDTO = new InstanceDTO(configurationId, userId, 5L, "192.168.1.100", ipHash);

        when(userService.findInstance(userId, configurationId, ipHash)).thenReturn(instance);
        when(instanceDTOConverter.toDTO(instance)).thenReturn(instanceDTO);

        mockMvc.perform(get("/api/user/{id}/instances/{configurationId}", userId, configurationId)
                        .param("hash", ipHash))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurationId").value(configurationId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.serverId").value(5L))
                .andExpect(jsonPath("$.ip").value("192.168.1.100"))
                .andExpect(jsonPath("$.ipHash").value(ipHash));
    }
}
