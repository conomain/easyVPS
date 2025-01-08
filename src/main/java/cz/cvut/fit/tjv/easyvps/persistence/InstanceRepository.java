package cz.cvut.fit.tjv.easyvps.persistence;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepository extends JpaRepository<Instance, Instance.InstanceId> {
}
