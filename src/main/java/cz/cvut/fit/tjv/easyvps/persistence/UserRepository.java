package cz.cvut.fit.tjv.easyvps.persistence;

import cz.cvut.fit.tjv.easyvps.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
