package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;

public interface UserServiceInterface extends CrudServiceInterface<User, Long> {

    User addInstanceToUser(Long configurationId, Long userId) throws IllegalArgumentException;

    User removeInstanceFromUser(Long userId, Long configurationId, String ipHash) throws IllegalArgumentException;

    Instance findInstance(Long userId, Long configurationId, String ipHash) throws IllegalArgumentException;
}
