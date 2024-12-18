package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.domain.UserConfiguration;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import cz.cvut.fit.tjv.easyvps.persistence.UserConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ServerService extends CrudServiceInterfaceImpl<Server, Long> implements ServerServiceInterface {

    private final ServerRepository serverRepository;
    private final ConfigurationRepository configurationRepository;
    private final UserConfigurationRepository userConfigurationRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository, ConfigurationRepository configurationRepository, UserConfigurationRepository userConfigurationRepository) {
        this.serverRepository = serverRepository;
        this.configurationRepository = configurationRepository;
        this.userConfigurationRepository = userConfigurationRepository;
    }

    @Override
    public Server addConfigurationToServer(Long configurationId) throws IllegalArgumentException {

        List<Server> servers = serverRepository.findAll();
        if (servers.isEmpty()) {
            throw new IllegalArgumentException("No servers found.");
        }

        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration with id " + configurationId + " does not exist."));

        for (Server server : servers) {
            long totalUsedCores = server.getConfigurations().stream().mapToLong(Configuration::getCpu_cores).sum();
            long totalUsedRam = server.getConfigurations().stream().mapToLong(Configuration::getRam).sum();
            long totalUsedStorage = server.getConfigurations().stream().mapToLong(Configuration::getStorage).sum();

            if (configuration.getCpu_cores() + totalUsedCores <= server.getCpu_cores() &&
                    configuration.getRam() + totalUsedRam <= server.getRam() &&
                    configuration.getStorage() + totalUsedStorage <= server.getStorage()) {

                server.getConfigurations().add(configuration);
                configuration.getServers().add(server);

                configurationRepository.save(configuration);
                return serverRepository.save(server);
            }
        }

        throw new IllegalArgumentException("No server has enough resources to add configuration with id " + configurationId);
    }

    @Override
    public Server removeConfigurationFromServer(List<Long> configurationIds) throws IllegalArgumentException {
        return null;
    }

    @Override
    protected CrudRepository<Server, Long> getRepository() {
        return serverRepository;
    }
}
