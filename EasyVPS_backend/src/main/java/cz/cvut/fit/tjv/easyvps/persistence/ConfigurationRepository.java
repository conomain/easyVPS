package cz.cvut.fit.tjv.easyvps.persistence;

import cz.cvut.fit.tjv.easyvps.domain.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
}
