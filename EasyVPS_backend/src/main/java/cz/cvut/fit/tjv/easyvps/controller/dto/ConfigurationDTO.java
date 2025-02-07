package cz.cvut.fit.tjv.easyvps.controller.dto;

import cz.cvut.fit.tjv.easyvps.domain.Instance;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ConfigurationDTO {
    private Long id;
    private String name;
    private Long cpu_cores;
    private Long ram;
    private Long storage;
    private Long price;
    private Set<Long> userIds;
}
