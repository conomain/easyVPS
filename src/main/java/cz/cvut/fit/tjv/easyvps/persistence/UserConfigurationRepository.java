package cz.cvut.fit.tjv.easyvps.persistence;

import cz.cvut.fit.tjv.easyvps.domain.UserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigurationRepository extends JpaRepository<UserConfiguration, Long> {
}
