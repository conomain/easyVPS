package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;

public interface UserServiceInterface extends CrudServiceInterface<User, Long> {

    public void addInstanceToUser(Long configurationId, Long userId) throws IllegalArgumentException;

    public void removeInstanceFromUser(Long configurationId, Long serverId, Long userId, String ip) throws IllegalArgumentException;
}
