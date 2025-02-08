package cz.cvut.fit.tjv.easyvps_client.client_api;

import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
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
public class ConfigurationClient {

    private RestClient configurationClient;

    public ConfigurationClient(@Value("${api.url}") String serverUrl) {
        configurationClient = RestClient.create(serverUrl + "/configuration");
    }

    public List<ConfigurationDTO> readAll() {
        return Arrays.asList(Objects.requireNonNull(configurationClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ConfigurationDTO[].class)
                .getBody()));
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
            System.out.println("Configuration not found.");
            return Optional.empty();
        }
    }

    public void create(ConfigurationDTO configurationDTO) {
        configurationClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(configurationDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(ConfigurationDTO configurationDTO) {
        configurationClient.put()
                .uri("/{id}", configurationDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(configurationDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(Long configurationId) {
        configurationClient.delete()
                .uri("/{id}", configurationId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();
    }
}
