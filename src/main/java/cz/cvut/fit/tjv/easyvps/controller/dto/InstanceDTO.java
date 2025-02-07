package cz.cvut.fit.tjv.easyvps.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InstanceDTO {
    private Long configurationId;
    private Long userId;
    private Long serverId;
    private String ip;
    private String ipHash;
}
