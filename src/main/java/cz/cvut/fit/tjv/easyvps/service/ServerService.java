package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Server;
import cz.cvut.fit.tjv.easyvps.persistence.ServerRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class ServerService extends CrudServiceInterfaceImpl<Server, Long> implements ServerServiceInterface {

    private ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }


    @Override
    protected CrudRepository<Server, Long> getRepository() {
        return serverRepository;
    }


}
