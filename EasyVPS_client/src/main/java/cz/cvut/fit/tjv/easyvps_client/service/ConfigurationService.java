package cz.cvut.fit.tjv.easyvps_client.service;

import cz.cvut.fit.tjv.easyvps_client.client_api.ConfigurationClient;
import cz.cvut.fit.tjv.easyvps_client.client_api.UserClient;
import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps_client.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationService {

    private ConfigurationClient configurationClient;
    private UserClient userClient;

    public ConfigurationService(ConfigurationClient configurationClient, UserClient userClient) {
        this.configurationClient = configurationClient;
        this.userClient = userClient;
    }

    public List<ConfigurationDTO> readAll() {
        return configurationClient.readAll();
    }

    public Optional<ConfigurationDTO> read(Long id) {
        return configurationClient.read(id);
    }

    public void create(ConfigurationDTO configurationDTO) {
        configurationClient.create(configurationDTO);
    }

    public void update(ConfigurationDTO configurationDTO) {
        configurationClient.update(configurationDTO);
    }

    public void delete(Long id) {
        configurationClient.delete(id);
    }

    public Optional<UserDTO> findUserById(Long userId) {
        return userClient.read(userId);
    }
}
