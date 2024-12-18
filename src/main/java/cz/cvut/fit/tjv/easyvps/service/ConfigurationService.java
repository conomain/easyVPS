package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.persistence.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService extends CrudServiceInterfaceImpl<Configuration, Long> implements ConfigurationServiceInterface {

    private final ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationService (ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    protected CrudRepository<Configuration, Long> getRepository() {
        return configurationRepository;
    }
}
