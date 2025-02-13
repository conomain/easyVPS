package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.InstanceRepository;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private InstanceRepository instanceRepository;

    @Mock
    private ServerService serverService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    public void testCreateUserSuccess() {
        assertNull(user.getId(), "User id should be null before creation");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(user.getUsername());
        savedUser.setEmail(user.getEmail());
        savedUser.setPassword(user.getPassword());
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.create(user);

        assertNotNull(result.getId(), "Created user should have an id");
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testCreateUserWithIdShouldThrowException() {
        user.setId(10L);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user);
        });
        assertTrue(exception.getMessage().contains("already exists"), "Exception message should indicate that user already exists");
    }

    @Test
    public void testCreateUserDuplicateUsernameShouldThrowException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user);
        });
        assertTrue(exception.getMessage().contains("username"), "Exception message should indicate duplicate username");
    }

    @Test
    public void testCreateUserDuplicateEmailShouldThrowException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user);
        });
        assertTrue(exception.getMessage().contains("email"), "Exception message should indicate duplicate email");
    }

    @Test
    public void testAddInstanceToUserSuccess() {
        Long userId = 1L;
        Long configurationId = 10L;
        String ipHash = "hash";

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        Configuration configuration = new Configuration();
        configuration.setId(configurationId);
        configuration.setName("testconfig");
        configuration.setCpu_cores(1L);
        configuration.setRam(1L);
        configuration.setStorage(1L);
        configuration.setPrice(1L);

        Server server = new Server();
        server.setId(100L);
        server.setCpu_cores(10L);
        server.setRam(10L);
        server.setStorage(10L);

        Instance instance = new Instance();
        instance.getId().setUserId(userId);
        instance.getId().setConfigurationId(configurationId);
        instance.getId().setIpHash(ipHash);
        instance.setUser(user);
        instance.setConfiguration(configuration);
        instance.setServer(server);
        instance.setIp("192.168.1.1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(configurationRepository.findById(configurationId)).thenReturn(Optional.of(configuration));
        when(serverService.findAvailableServer(any(Configuration.class))).thenReturn(Optional.of(server));
        doNothing().when(serverService).allocateServerResources(server, configuration);
        when(instanceRepository.save(any(Instance.class))).thenReturn(instance);

        User resultUser = userService.addInstanceToUser(configurationId, userId);

        assertNotNull(resultUser, "User should not be null after adding instance");
        assertFalse(resultUser.getInstances().contains(instance), "User should have the unique instance");
        verify(instanceRepository).save(any(Instance.class));
    }

}
