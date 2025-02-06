package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "instances")
@Getter
@Setter
public class Instance {

    @EmbeddedId
    private InstanceId id = new InstanceId();

    @MapsId("serverId")
    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @MapsId("configurationId")
    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "instance_ip")
    private String ip;


    @Getter
    @Setter
    @Embeddable
    public static class InstanceId implements Serializable {

        private Long serverId;
        private Long configurationId;
        private Long userId;
        private String ip;

        public InstanceId() {
        }

        public InstanceId(Long configurationId, Long serverId, Long userId, String ip) {
            super();
            this.configurationId = configurationId;
            this.serverId = serverId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InstanceId that = (InstanceId) o;
            return Objects.equals(serverId, that.serverId) &&
                    Objects.equals(configurationId, that.configurationId) &&
                    Objects.equals(userId, that.userId) &&
                    Objects.equals(ip, that.ip);
        }


        @Override
        public int hashCode() {
            return Objects.hash(serverId, configurationId, userId, ip);
        }
    }
}
