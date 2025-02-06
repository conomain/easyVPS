package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.*;
import cz.cvut.fit.tjv.easyvps.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService extends CrudServiceInterfaceImpl<User, Long> implements UserServiceInterface {

    private final UserRepository userRepository;
    private final ConfigurationRepository configurationRepository;
    private final ServerRepository serverRepository;
    private final InstanceRepository instanceRepository;

    private ServerService serverService;


    @Override
    public User create(User user) throws IllegalArgumentException {
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists.");
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long userId) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist."));

        Set<Instance> instances = user.getInstances();

        for (Instance instance : instances) {
            Server server = instance.getServer();
            serverService.freeServerResources(server, instance.getConfiguration());
            server.getInstances().remove(instance);
            serverRepository.save(server);

            Configuration configuration = instance.getConfiguration();
            configuration.getInstances().remove(instance);
            configurationRepository.save(configuration);

            user.getInstances().remove(instance);
            userRepository.save(user);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public void addInstanceToUser(Long configurationId, Long userId) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist."));

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + configurationId + " does not exist."));

        Server server = serverService.findAvailableServer(configuration)
                .orElseThrow(() -> new IllegalArgumentException("No available server for configuration."));

        serverService.allocateServerResources(server, configuration);

        String newIp = generateUniqueIp();

        Instance.InstanceId instanceId = new Instance.InstanceId(configurationId, server.getId(), userId, newIp);
        Instance instance = new Instance();
        instance.setId(instanceId);
        instance.setServer(server);
        instance.setConfiguration(configuration);
        instance.setUser(user);
        instance.setIp(newIp);

        instanceRepository.save(instance);

        user.getInstances().add(instance);
        configuration.getInstances().add(instance);
        server.getInstances().add(instance);

        userRepository.save(user);
        configurationRepository.save(configuration);
        serverRepository.save(server);
    }

    private String generateUniqueIp() {
        String ip;
        do {
            ip = "192.168." + (int) (Math.random() * 256) + "." + (int) (Math.random() * 256);
        }
        while (instanceRepository.existsByIp(ip));
        return ip;
    }

    @Override
    public void removeInstanceFromUser(Long configurationId, Long serverId, Long userId, String ip) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist."));

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + configurationId + " does not exist."));

        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new IllegalArgumentException("Server with id " + serverId + " does not exist."));

        Instance.InstanceId instanceId = new Instance.InstanceId(configurationId, serverId, userId, ip);
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Instance with ip " + ip + " does not exist."));


        serverService.freeServerResources(server, instance.getConfiguration());

        user.getInstances().remove(instance);
        configuration.getInstances().remove(instance);
        server.getInstances().remove(instance);

        //instanceRepository.delete(instance);

        userRepository.save(user);
        configurationRepository.save(configuration);
        serverRepository.save(server);
    }

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}
