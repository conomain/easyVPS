package cz.cvut.fit.tjv.easyvps.service;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import cz.cvut.fit.tjv.easyvps.domain.User;
import jakarta.persistence.EntityNotFoundException;

public interface UserServiceInterface extends CrudServiceInterface<User, Long> {

    User addInstanceToUser(Long configurationId, Long userId) throws EntityNotFoundException;

    User removeInstanceFromUser(Long userId, Long configurationId, String ipHash) throws EntityNotFoundException;

    Instance findInstance(Long userId, Long configurationId, String ipHash) throws EntityNotFoundException;
}
