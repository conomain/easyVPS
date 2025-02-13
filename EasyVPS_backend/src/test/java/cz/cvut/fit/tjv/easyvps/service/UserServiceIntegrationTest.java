package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("integrationUser");
        user.setEmail("integration@example.com");
        user.setPassword("password");

        User createdUser = userService.create(user);
        assertNotNull(createdUser.getId(), "Created user should have an id");

        Optional<User> fromDb = userRepository.findById(createdUser.getId());
        assertTrue(fromDb.isPresent(), "User should be present in repository");
        assertEquals("integrationUser", fromDb.get().getUsername(), "Username should match");
        assertEquals("integration@example.com", fromDb.get().getEmail(), "Email should match");
    }

    @Test
    public void testDuplicateUsername() {
        User user1 = new User();
        user1.setUsername("duplicateUser");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        userService.create(user1);

        User user2 = new User();
        user2.setUsername("duplicateUser");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user2);
        });
        assertTrue(exception.getMessage().contains("already exists"), "Exception message should indicate duplicate username");
    }

    @Test
    public void testDuplicateEmail() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("duplicate@example.com");
        user1.setPassword("password");
        userService.create(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("duplicate@example.com");
        user2.setPassword("password");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(user2);
        });
        assertTrue(exception.getMessage().contains("already exists"), "Exception message should indicate duplicate email");
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("deleteUser");
        user.setEmail("delete@example.com");
        user.setPassword("password");
        User created = userService.create(user);
        Long userId = created.getId();

        assertTrue(userRepository.findById(userId).isPresent(), "User should exist before deletion");

        userService.deleteById(userId);

        Optional<User> deleted = userRepository.findById(userId);
        assertFalse(deleted.isPresent(), "User should be deleted from repository");
    }

    @Test
    @Transactional
    public void testAddInstanceToUser() {
        User user = new User();
        user.setUsername("integrationUser");
        user.setEmail("integration@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Configuration configuration = new Configuration();
        configuration.setName("Test Configuration");
        configuration.setCpu_cores(1L);
        configuration.setRam(1L);
        configuration.setStorage(1L);
        configuration.setPrice(1L);
        configurationRepository.save(configuration);

        Server server = new Server();
        server.setCpu_cores(4L);
        server.setRam(4096L);
        server.setStorage(100000L);
        serverRepository.save(server);

        User resultUser = userService.addInstanceToUser(configuration.getId(), user.getId());

        assertNotNull(resultUser, "User should not be null after adding instance");
        assertEquals(1, resultUser.getInstances().size(), "User should have exactly one instance");
        Instance instance = resultUser.getInstances().iterator().next();
        assertEquals(configuration.getId(), instance.getConfiguration().getId(), "Instance should have correct configuration");
    }
}
