package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.*;
import cz.cvut.fit.tjv.easyvps.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ServerService extends CrudServiceInterfaceImpl<Server, Long> implements ServerServiceInterface {

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ConfigurationRepository configurationRepository;

    @Override
    public void deleteById(Long id) throws IllegalArgumentException {

        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Server with id " + id + " does not exist."));

        Set<Instance> instances = server.getInstances();

        for (Instance instance : instances) {
            User user = instance.getUser();
            user.getInstances().remove(instance);
            userRepository.save(user);

            Configuration configuration = instance.getConfiguration();
            configuration.getInstances().remove(instance);
            configurationRepository.save(configuration);

            server.getInstances().remove(instance);
            freeServerResources(server, instance.getConfiguration());
            serverRepository.save(server);
        }

        serverRepository.deleteById(id);
    }


    public Optional<Server> findAvailableServer(Configuration configuration) {
        return serverRepository.findAll().stream()
                .filter(server -> hasEnoughResources(server, configuration))
                .findFirst();
    }

    private boolean hasEnoughResources(Server server, Configuration configuration) {
        return server.getCpu_cores() >= configuration.getCpu_cores() &&
                server.getRam() >= configuration.getRam() &&
                server.getStorage() >= configuration.getStorage();
    }

    public void allocateServerResources(Server server, Configuration configuration) throws IllegalArgumentException {
        if (!hasEnoughResources(server, configuration)) {
            throw new IllegalArgumentException("Not enough resources on server.");
        }
        server.setCpu_cores(server.getCpu_cores() - configuration.getCpu_cores());
        server.setRam(server.getRam() - configuration.getRam());
        server.setStorage(server.getStorage() - configuration.getStorage());
        serverRepository.save(server);
    }

    public void freeServerResources(Server server, Configuration configuration) {
        server.setCpu_cores(server.getCpu_cores() + configuration.getCpu_cores());
        server.setRam(server.getRam() + configuration.getRam());
        server.setStorage(server.getStorage() + configuration.getStorage());
        serverRepository.save(server);
    }


    @Override
    protected CrudRepository<Server, Long> getRepository() {
        return serverRepository;
    }
}
