package cz.cvut.fit.tjv.easyvps_client.service;

import cz.cvut.fit.tjv.easyvps_client.client_api.ConfigurationClient;
import cz.cvut.fit.tjv.easyvps_client.client_api.UserClient;
import cz.cvut.fit.tjv.easyvps_client.model.ConfigurationDTO;
import cz.cvut.fit.tjv.easyvps_client.model.InstanceDTO;
import cz.cvut.fit.tjv.easyvps_client.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private ConfigurationClient configurationClient;

    private UserClient userClient;


    public UserService(UserClient userClient, ConfigurationClient configurationClient1) {
        this.userClient = userClient;
        this.configurationClient = configurationClient1;
    }

    public List<UserDTO> readAll() {
        return userClient.readAll();
    }

    public Optional<UserDTO> read(Long id) {
        return userClient.read(id);
    }

    public void create(UserDTO userDTO) {
        userClient.create(userDTO);
    }

    public void update(UserDTO userDTO) {
        userClient.update(userDTO);
    }

    public void delete(Long id) {
        userClient.delete(id);
    }

    public List<InstanceDTO> getUserInstances(Long userId) {
        return userClient.getUserInstances(userId);
    }

    public Optional<InstanceDTO> getUserInstance(Long userId, Long configurationId, String ipHash) {
        return userClient.getInstance(userId, configurationId, ipHash);
    }

    public void addInstanceToUser(Long userId, Long configurationId) {
        userClient.addInstanceToUser(userId, configurationId);
    }

    public void removeInstanceFromUser(Long userId, Long configurationId, String ipHash) {
        userClient.removeInstanceFromUser(userId, configurationId, ipHash);
    }

    public Optional<ConfigurationDTO> findConfigurationById(Long configurationId) {
        return configurationClient.read(configurationId);
    }
}
