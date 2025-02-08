package cz.cvut.fit.tjv.easyvps_client.service;

import cz.cvut.fit.tjv.easyvps_client.client_api.ServerClient;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.ServerDTO;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.List;
import java.util.Optional;

@Service
public class ServerService {

    private ServerClient serverClient;

    public ServerService(ServerClient serverClient) {
        this.serverClient = serverClient;
    }

    public List<ServerDTO> readAll() {
        return serverClient.readAll();
    }

    public Optional<ServerDTO> read(Long id) {
        return serverClient.read(id);
    }

    public List<InstanceDTO> getServerInstances(Long id) {
        return serverClient.getServerInstances(id);
    }

    public void createServer(ServerDTO serverDTO) {
        serverClient.create(serverDTO);
    }

    public void updateServer(ServerDTO serverDTO) {
        serverClient.update(serverDTO);
    }

    public void deleteServer(Long id) {
        serverClient.delete(id);
    }
}
