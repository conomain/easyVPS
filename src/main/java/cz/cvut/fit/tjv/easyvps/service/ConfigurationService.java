package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import org.springframework.data.repository.CrudRepository;

public class ConfigurationService extends CrudServiceInterfaceImpl<Configuration, Long> implements ConfigurationServiceInterface {

    @Override
    protected CrudRepository<Configuration, Long> getRepository() {
        return null;
    }

    @Override
    public void addConfigurationToUser(Long configurationId, Long userId) {

    }

    @Override
    public void removeConfigurationFromUser(Long configurationId, Long userId) {

    }
}
