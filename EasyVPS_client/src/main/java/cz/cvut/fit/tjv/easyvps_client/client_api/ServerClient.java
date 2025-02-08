package cz.cvut.fit.tjv.easyvps_client.client_api;

import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.ServerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class ServerClient {

    private RestClient serverClient;

    public ServerClient(@Value("${api.url}") String serverUrl) {
        serverClient = RestClient.create(serverUrl + "/server");
    }

    public List<ServerDTO> readAll() {
        return Arrays.asList(Objects.requireNonNull(serverClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ServerDTO[].class)
                .getBody()));
    }

    public Optional<ServerDTO> read(Long serverId) {
        try {
            return Optional.ofNullable(serverClient.get()
                    .uri( "/{id}", serverId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ServerDTO.class)
                    .getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Server not found.");
            return Optional.empty();
        }
    }

    public List<InstanceDTO> getServerInstances(Long serverId) {
        return Arrays.asList(Objects.requireNonNull(serverClient.get()
                .uri("/{id}/instances", serverId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(InstanceDTO[].class)));
    }

    public void create(ServerDTO serverDTO) {
        serverClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serverDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(ServerDTO serverDTO) {
        serverClient.put()
                .uri("/{id}", serverDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(serverDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(Long serverId) {
        serverClient.delete()
                .uri("/{id}", serverId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
    }
}
