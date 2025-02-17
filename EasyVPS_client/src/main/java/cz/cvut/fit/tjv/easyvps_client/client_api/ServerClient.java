package cz.cvut.fit.tjv.easyvps_client.client_api;

import cz.cvut.fit.tjv.easyvps_client.exception.InvalidRequestException;
import cz.cvut.fit.tjv.easyvps_client.exception.ResourceNotFoundException;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.ServerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class ServerClient extends JSONParser {

    private final RestClient serverClient;

    public ServerClient(@Value("${api.url}") String serverUrl) {
        serverClient = RestClient.create(serverUrl + "/server");
    }

    public List<ServerDTO> readAll() {
        try {
            return Arrays.asList(Objects.requireNonNull(serverClient.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ServerDTO[].class)
                    .getBody()));
        } catch (HttpClientErrorException e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error reading servers: " + apiMessage);
        }
    }

    public Optional<ServerDTO> read(Long serverId) {
        try {
            return Optional.ofNullable(serverClient.get()
                    .uri("/{id}", serverId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ServerDTO.class)
                    .getBody());
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Server with id " + serverId + " not found");
        }
    }

    public List<InstanceDTO> getServerInstances(Long serverId) {
        try {
            return Arrays.asList(Objects.requireNonNull(serverClient.get()
                    .uri("/{id}/instances", serverId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(InstanceDTO[].class)
                    .getBody()));
        } catch (HttpClientErrorException e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error reading server instances: " + apiMessage);
        }
    }

    public void create(ServerDTO serverDTO) {
        try {
            serverClient.post()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(serverDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error creating server: " + apiMessage);
        }
    }

    public void update(ServerDTO serverDTO) {
        try {
            serverClient.put()
                    .uri("/{id}", serverDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(serverDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Server with id " + serverDTO.getId() + " not found.");
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error updating server: " + apiMessage);
        }
    }

    public void delete(Long serverId) {
        try {
            serverClient.delete()
                    .uri("/{id}", serverId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Server with id " + serverId + " not found.");
        }
    }
}
