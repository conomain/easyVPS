package cz.cvut.fit.tjv.easyvps.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "instances")
@Getter
@Setter
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @Column(name = "instance_ip")
    private String ip;

    @Getter
    @Setter
    @Embeddable
    public static class InstanceId implements Serializable {
        private Long userId;
        private Long configurationId;
        private String ipHash;

        public InstanceId() {}

        public InstanceId(Long userId, Long configurationId, String ipHash) {
            this.userId = userId;
            this.configurationId = configurationId;
            this.ipHash = ipHash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InstanceId)) return false;
            InstanceId that = (InstanceId) o;
            return  Objects.equals(userId, that.userId) &&
                    Objects.equals(configurationId, that.configurationId) &&
                    Objects.equals(ipHash, that.ipHash);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, configurationId, ipHash);
        }
    }
}
