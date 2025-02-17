package cz.cvut.fit.tjv.easyvps_client.client_api;

import cz.cvut.fit.tjv.easyvps_client.exception.InvalidRequestException;
import cz.cvut.fit.tjv.easyvps_client.exception.ResourceNotFoundException;
import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class ConfigurationClient extends JSONParser {

    private final RestClient configurationClient;

    public ConfigurationClient(@Value("${api.url}") String serverUrl) {
        configurationClient = RestClient.create(serverUrl + "/configuration");
    }

    public List<ConfigurationDTO> readAll() {
        try {
            return Arrays.asList(Objects.requireNonNull(configurationClient.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ConfigurationDTO[].class)
                    .getBody()));
        } catch (HttpClientErrorException e) {
            throw new InvalidRequestException("Error reading configurations: " + e.getResponseBodyAsString());
        }
    }

    public Optional<ConfigurationDTO> read(Long configurationId) {
        try {
            return Optional.ofNullable(configurationClient.get()
                    .uri("/{id}", configurationId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(ConfigurationDTO.class)
                    .getBody());
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Configuration with id " + configurationId + " not found");
        }
    }

    public void create(ConfigurationDTO configurationDTO) {
        try {
            configurationClient.post()
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(configurationDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error creating configuration: " + apiMessage);
        }
    }

    public void update(ConfigurationDTO configurationDTO) {
        try {
            configurationClient.put()
                    .uri("/{id}", configurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(configurationDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Configuration with id " + configurationDTO.getId() + " not found.");
        } catch (HttpClientErrorException.BadRequest e) {
            String apiMessage = getApiMessage(e.getResponseBodyAsString());
            throw new InvalidRequestException("Error updating configuration: " + apiMessage);
        }
    }

    public void delete(Long configurationId) {
        try {
            configurationClient.delete()
                    .uri("/{id}", configurationId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Configuration with id " + configurationId + " not found.");
        }
    }
}
