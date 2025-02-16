package cz.cvut.fit.tjv.easyvps_client.client_api;

import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserClient {
    private RestClient userClient;

    public UserClient(@Value("${api.url}") String baseUrl) {
        userClient = RestClient.create(baseUrl + "/user");
    }

    public List<UserDTO> readAll() {
        return Arrays.asList(Objects.requireNonNull(userClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(UserDTO[].class)
                .getBody()));
    }

    public Optional<UserDTO> read(Long userId) {
        try {
            return Optional.ofNullable(userClient.get()
                    .uri("/{id}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(UserDTO.class)
                    .getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("User not found.");
            return Optional.empty();
        }
    }

    public void create(UserDTO userDTO) {
        userClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(UserDTO userDTO) {
        userClient.put()
                .uri("/{id}", userDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(Long userId) {
        userClient.delete()
                .uri("/{id}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
    }

    public List<InstanceDTO> getUserInstances(Long userId) {
        return Arrays.asList(Objects.requireNonNull(userClient.get()
                .uri("/{id}/instances", userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(InstanceDTO[].class)
                .getBody()));
    }

    public Optional<InstanceDTO> getInstance(Long userId, Long configurationId, String ipHash) {
        try {
            return Optional.ofNullable(userClient.get()
                    .uri("/{id}/instances/{configId}?hash={ipHash}", userId, configurationId, ipHash)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(InstanceDTO.class)
                    .getBody());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Instance not found.");
            return Optional.empty();
        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("Bad request: " + e.getResponseBodyAsString());
            throw new RuntimeException("Invalid request: " + e.getResponseBodyAsString());
        }
    }

    public void addInstanceToUser(Long userId, Long configurationId) {
        try {
            userClient.post()
                    .uri("/{id}/start?configurationId={configId}", userId, configurationId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("Bad request: " + e.getResponseBodyAsString());
            throw new RuntimeException("No available server for configuration: " + e.getResponseBodyAsString());
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("User or configuration not found.");
            throw new RuntimeException("User or configuration not found.");
        }
    }

    public void removeInstanceFromUser(Long userId, Long configurationId, String ipHash) {
        try {
            userClient.delete()
                    .uri("/{id}/stop?configurationId={configId}&hash={ipHash}", userId, configurationId, ipHash)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("User or configuration not found.");
            throw new RuntimeException("User or configuration not found.");
        }
    }
}
