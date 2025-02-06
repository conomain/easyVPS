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


    @MapsId("configurationId")
    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "instance_quantity")
    private Long quantity;

    @Column(name = "instance_ip")
    private String ip;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @Getter
    @Setter
    @Embeddable
    public static class InstanceId implements Serializable {
        private Long userId;
        private Long configurationId;

        public InstanceId() {}

        public InstanceId(Long userId, Long configurationId) {
            this.userId = userId;
            this.configurationId = configurationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InstanceId)) return false;
            InstanceId that = (InstanceId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(configurationId, that.configurationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, configurationId);
        }
    }
}
