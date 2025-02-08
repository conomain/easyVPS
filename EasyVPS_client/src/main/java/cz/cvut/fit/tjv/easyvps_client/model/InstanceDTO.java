package cz.cvut.fit.tjv.easyvps_client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstanceDTO {
    private Long configurationId;
    private Long userId;
    private Long serverId;
    private String ip;
    private String ipHash;
}
