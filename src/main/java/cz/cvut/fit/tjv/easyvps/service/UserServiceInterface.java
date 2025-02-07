package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;

public interface UserServiceInterface extends CrudServiceInterface<User, Long> {

    void addInstanceToUser(Long configurationId, Long userId) throws IllegalArgumentException;

    void removeInstanceFromUser(Long userId, Long configurationId) throws IllegalArgumentException;
}
