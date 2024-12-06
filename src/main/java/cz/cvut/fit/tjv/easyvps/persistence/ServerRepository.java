package cz.cvut.fit.tjv.easyvps.persistence;

import cz.cvut.fit.tjv.easyvps.domain.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
}
