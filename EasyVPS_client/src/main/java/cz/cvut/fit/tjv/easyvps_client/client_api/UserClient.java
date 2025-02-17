package cz.cvut.fit.tjv.easyvps_client.client_api;

import cz.cvut.fit.tjv.easyvps_client.exception.InvalidRequestException;
import cz.cvut.fit.tjv.easyvps_client.exception.ResourceNotFoundException;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class UserClient extends JSONParser{

    private final RestClient userClient;

    public UserClient(@Value("${api.url}") String baseUrl) {
        userClient = RestClient.create(baseUrl + "/user");
    }

    public List<UserDTO> readAll() {
        try {
            return Arrays.asList(Objects.requireNonNull(userClient.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(UserDTO[].class)
                    .getBody()));
        } catch (HttpClientErrorException e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error reading users: " + apiMessage);
        }
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
            throw new ResourceNotFoundException("User with id " + userId + " not found.");
        }
    }

    public void create(UserDTO userDTO) {
        try {
            userClient.post()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error creating user: " + apiMessage);
        }
    }

    public void update(UserDTO userDTO) {
        try {
            userClient.put()
                    .uri("/{id}", userDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("User with id " + userDTO.getId() + " not found.");
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error updating user: " + apiMessage);
        }
    }

    public void delete(Long userId) {
        try {
            userClient.delete()
                    .uri("/{id}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("User with id " + userId + " not found.");
        }
    }

    public List<InstanceDTO> getUserInstances(Long userId) {
        try {
            return Arrays.asList(Objects.requireNonNull(userClient.get()
                    .uri("/{id}/instances", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(InstanceDTO[].class)
                    .getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("User with id " + userId + " not found.");
        }
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
            throw new ResourceNotFoundException("Instance not found.");
        }
    }

    public void addInstanceToUser(Long userId, Long configurationId) {
        try {
            userClient.post()
                    .uri("/{id}/start?configurationId={configId}", userId, configurationId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("User or configuration not found.");
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException(apiMessage);
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
            throw new ResourceNotFoundException("Instance not found.");
        }
    }
}
