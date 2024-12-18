package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_configuration")
@Getter
@Setter
public class UserConfiguration {

    @EmbeddedId
    private UserConfigurationId id = new UserConfigurationId();

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("configurationId")
    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    @Column(name = "quantity")
    private Long quantity;


    @Getter
    @Setter
    @Embeddable
    public static class UserConfigurationId implements Serializable {

        private Long userId;
        private Long configurationId;

        public UserConfigurationId() {}

        public UserConfigurationId(Long bookId, Long userId) {
            super();
            this.userId = userId;
            this.configurationId = bookId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserConfigurationId that = (UserConfigurationId) o;

            if (!userId.equals(that.userId)) return false;
            return configurationId.equals(that.configurationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, configurationId);
        }
    }
}
