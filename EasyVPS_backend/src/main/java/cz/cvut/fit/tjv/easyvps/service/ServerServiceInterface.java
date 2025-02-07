package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.Server;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ServerServiceInterface extends CrudServiceInterface<Server, Long> {

    void allocateServerResources(Server server, Configuration configuration) throws IllegalArgumentException;

    void freeServerResources(Server server, Configuration configuration);

    Optional<Server> findAvailableServer(Configuration configuration);
}
