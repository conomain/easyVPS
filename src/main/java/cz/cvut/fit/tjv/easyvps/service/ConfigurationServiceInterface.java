package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;

public interface ConfigurationServiceInterface extends CrudServiceInterface<Configuration, Long> {

     public void addConfigurationToUser(Long configurationId, Long userId);

     public void removeConfigurationFromUser(Long configurationId, Long userId);

}
