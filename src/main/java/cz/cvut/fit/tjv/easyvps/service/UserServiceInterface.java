package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.UserConfiguration;
import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import cz.cvut.fit.tjv.easyvps.domain.User;

public interface UserServiceInterface extends CrudServiceInterface<User, Long> {

    public void addConfigurationsToUser(Long configurationId, Long userId, Long quantity) throws IllegalArgumentException;
    public void removeConfigurationsFromUser(Long configurationId, Long userId, Long quantity) throws IllegalArgumentException;

}
