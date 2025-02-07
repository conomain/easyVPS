package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.domain.User;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class ConfigurationService extends CrudServiceInterfaceImpl<Configuration, Long> implements ConfigurationServiceInterface {

    private final ConfigurationRepository configurationRepository;
    private final ServerRepository serverRepository;
    private final UserRepository userRepository;

    private ServerService serverService;

    @Override
    public void deleteById(Long id) throws IllegalArgumentException {

        Configuration configuration = configurationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + id + " does not exist."));

        Set<Instance> instances = configuration.getInstances();

        for (Instance instance : instances) {
            User user = instance.getUser();
            user.getInstances().remove(instance);
            userRepository.save(user);

            Server server = instance.getServer();
            serverService.freeServerResources(server, instance.getConfiguration());
            serverRepository.save(server);

            configuration.getInstances().remove(instance);
            configurationRepository.save(configuration);
        }

        configurationRepository.deleteById(id);
    }


    @Override
    protected CrudRepository<Configuration, Long> getRepository() {
        return configurationRepository;
    }
}
