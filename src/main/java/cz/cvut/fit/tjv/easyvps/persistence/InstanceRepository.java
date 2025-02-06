package cz.cvut.fit.tjv.easyvps.persistence;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface InstanceRepository extends JpaRepository<Instance, Instance.InstanceId> {

    boolean existsByIp(String ip);
}
