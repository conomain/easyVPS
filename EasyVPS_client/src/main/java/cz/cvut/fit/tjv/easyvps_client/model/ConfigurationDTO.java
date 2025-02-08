package cz.cvut.fit.tjv.easyvps_client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationDTO {
    private Long id;
    private String name;
    private Long cpu_cores;
    private Long ram;
    private Long storage;
    private Long price;
    private Set<Long> userIds;
}
