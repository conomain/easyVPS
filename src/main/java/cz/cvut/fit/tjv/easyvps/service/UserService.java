package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.domain.UserConfiguration;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService extends CrudServiceInterfaceImpl<User, Long> implements UserServiceInterface {

    private final UserRepository userRepository;
    private final ConfigurationRepository configurationRepository;
    private final UserConfigurationRepository userConfigurationRepository;

    @Autowired
    public UserService(UserRepository userRepository, ConfigurationRepository configurationRepository, UserConfigurationRepository userConfigurationRepository) {
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.userConfigurationRepository = userConfigurationRepository;
    }


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
    public void deleteById(Long id) throws IllegalArgumentException {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }

        throw new IllegalArgumentException("User with id " + id + " does not exist.");
    }

    @Override
    public void addConfigurationsToUser(Long configurationId, Long userId, Long quantity) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist."));

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + configurationId + " does not exist."));

        if (userConfigurationRepository.existsByConfigurationIdAndUserId(configurationId, userId)) {
            UserConfiguration.UserConfigurationId userConfigurationId = new UserConfiguration.UserConfigurationId(configurationId, userId);
            UserConfiguration userConfiguration = userConfigurationRepository.findById(userConfigurationId).get();

            userConfiguration.setQuantity(userConfiguration.getQuantity() + quantity);
            userConfigurationRepository.save(userConfiguration);
            return;
        }

        UserConfiguration.UserConfigurationId userConfigurationId = new UserConfiguration.UserConfigurationId(configurationId, userId);

        UserConfiguration userConfiguration = new UserConfiguration();
        userConfiguration.setId(userConfigurationId);
        userConfiguration.setConfiguration(configuration);
        userConfiguration.setUser(user);
        userConfiguration.setQuantity(quantity);
        userConfigurationRepository.save(userConfiguration);

        configuration.getUsers().add(userConfiguration);
        configurationRepository.save(configuration);

        user.getConfigurations().add(userConfiguration);
        userRepository.save(user);
    }

    @Override
    public void removeConfigurationsFromUser(Long configurationId, Long userId, Long quantity) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist."));

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + configurationId + " does not exist."));

        if (!userConfigurationRepository.existsByConfigurationIdAndUserId(configurationId, userId)) {
            throw new IllegalArgumentException("User with id " + userId + " does not have configuration with id " + configurationId + ".");
        }

        UserConfiguration.UserConfigurationId userConfigurationId = new UserConfiguration.UserConfigurationId(configurationId, userId);
        UserConfiguration userConfiguration = userConfigurationRepository.findById(userConfigurationId).get();
        Long configurationQuantity = userConfiguration.getQuantity();

        if (configurationQuantity < quantity) {
            throw new IllegalArgumentException("User with id " + userId + " does not have enough configuration with id " + configurationId + ".");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        if (configurationQuantity.equals(quantity)) {
            userConfigurationRepository.deleteById(userConfigurationId);

            user.getConfigurations().remove(userConfiguration);
            userRepository.save(user);

            configuration.getUsers().remove(userConfiguration);
            configurationRepository.save(configuration);
        }
        else {
            userConfiguration.setQuantity(configurationQuantity - quantity);
            userConfigurationRepository.save(userConfiguration);
        }

        userRepository.save(user);
    }

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}
