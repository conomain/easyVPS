package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.*;
import cz.cvut.fit.tjv.easyvps.persistence.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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

        Optional<Long> id = Optional.ofNullable(user.getId());

        if (id.isPresent()) {
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
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " does not exist."));

        Iterator<Instance> iterator = user.getInstances().iterator();

        while (iterator.hasNext()) {
            Instance instance = iterator.next();

            Server server = instance.getServer();
            serverService.freeServerResources(server, instance.getConfiguration());
            server.getInstances().remove(instance);
            serverRepository.save(server);

            Configuration configuration = instance.getConfiguration();
            configuration.getInstances().remove(instance);
            configurationRepository.save(configuration);

            iterator.remove();
        }

        userRepository.save(user);
        userRepository.deleteById(userId);
    }

    public User find(Long userId) {
        if (userRepository.existsById(userId)) {
            return userRepository.findById(userId).get();
        } else {
            throw new EntityNotFoundException("User with id " + userId + " does not exist.");
        }
    }

    @Override
    public User addInstanceToUser(Long configurationId, Long userId) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " does not exist."));

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new EntityNotFoundException("Configuration with id " + configurationId + " does not exist."));

        Server server = serverService.findAvailableServer(configuration)
                .orElseThrow(() -> new EntityNotFoundException("No available server for configuration."));

        serverService.allocateServerResources(server, configuration);

        String newIp = generateUniqueIp();
        String ipHash = String.valueOf(newIp.hashCode());

        Instance.InstanceId instanceId = new Instance.InstanceId(userId, configurationId, ipHash);

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

        return user;
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
    public User removeInstanceFromUser(Long userId, Long configurationId, String ipHash) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " does not exist."));

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new EntityNotFoundException("Configuration with id " + configurationId + " does not exist."));

        Instance.InstanceId instanceId = new Instance.InstanceId(userId, configurationId, ipHash);
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Instance for user id " + userId +
                        " and configuration id " + configurationId + " and ipHash " + ipHash + " does not exist."));


        Server server = instance.getServer();
        serverService.freeServerResources(server, configuration);

        user.getInstances().remove(instance);
        configuration.getInstances().remove(instance);
        server.getInstances().remove(instance);

        instanceRepository.delete(instance);

        userRepository.save(user);
        configurationRepository.save(configuration);
        serverRepository.save(server);

        return user;
    }

    @Override
    public Instance findInstance(Long userId, Long configurationId, String ipHash) {
        Instance.InstanceId instanceId = new Instance.InstanceId(userId, configurationId, ipHash);
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Instance for user id " + userId +
                        " and configuration id " + configurationId + " and ipHash " + ipHash + " does not exist."));
    }

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}
