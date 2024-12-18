package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Server;

import java.util.List;
import java.util.Map;

public interface ServerServiceInterface extends CrudServiceInterface<Server, Long> {

    public Server addConfigurationToServer(Long configurationId) throws IllegalArgumentException;
    public Server removeConfigurationFromServer(List<Long> configurationsId) throws IllegalArgumentException;

}
