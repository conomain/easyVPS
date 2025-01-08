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

    @Column(name = "instance_ip")
    private String ip;


    @Getter
    @Setter
    @Embeddable
    public static class InstanceId implements Serializable {

        private Long serverId;
        private Long configurationId;

        public InstanceId() {}

        public InstanceId(Long configurationId, Long serverId) {
            super();
            this.configurationId = configurationId;
            this.serverId = serverId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InstanceId that = (InstanceId) o;

            if (!configurationId.equals(that.configurationId)) return false;
            return serverId.equals(that.serverId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(serverId, configurationId);
        }
    }
}
