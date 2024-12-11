package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService extends CrudServiceInterfaceImpl<User, Long> implements UserServiceInterface {

    private final UserRepository userRepository;

    private final ConfigurationRepository configurationRepository;

    private final ServerRepository serverRepository;

    @Autowired
    public UserService(UserRepository userRepository, ConfigurationRepository configurationRepository, ServerRepository serverRepository) {
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.serverRepository = serverRepository;
    }

    @Override
    public User create(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists.");
        }
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists.");
        }

        return getRepository().save(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }
        getRepository().deleteById(id);
    }

    @Override
    protected CrudRepository<User, Long> getRepository() {
        return userRepository;
    }
}
