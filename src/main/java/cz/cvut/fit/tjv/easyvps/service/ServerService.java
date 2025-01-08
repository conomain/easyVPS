package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.domain.UserConfiguration;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import cz.cvut.fit.tjv.easyvps.persistence.InstanceRepository;
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
    private final InstanceRepository instanceRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository, ConfigurationRepository configurationRepository, InstanceRepository instanceRepository) {
        this.serverRepository = serverRepository;
        this.configurationRepository = configurationRepository;
        this.instanceRepository = instanceRepository;
    }

    @Override
    public Server addConfigurationToServer(Long configurationId) throws IllegalArgumentException {


        List<Server> servers = serverRepository.findAll();
        if (servers.isEmpty()) {
            throw new IllegalArgumentException("No servers found.");
        }
        for (s)
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
