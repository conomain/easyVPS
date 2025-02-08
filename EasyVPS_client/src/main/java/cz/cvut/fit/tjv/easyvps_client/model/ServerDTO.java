package cz.cvut.fit.tjv.easyvps_client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ServerDTO {
    private Long id;
    private Long cpu_cores;
    private Long ram;
    private Long storage;
    private Map<String, Long> instances;
}
