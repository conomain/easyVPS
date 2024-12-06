package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements EntityWithId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_user")
    private Long id;

    private String username;

    private String email;

    private String password;

    private String role;


    @ManyToMany(targetEntity = UserConfiguration.class)
    @JoinTable(
            name = "user_conf",
            joinColumns = @JoinColumn(name = "id_user_configuration"), // another table
            inverseJoinColumns = @JoinColumn(name = "id_user") // this table
    )
    private List<User> user_configurations;


    @Override
    public Long getId() {
        return id;
    }
}
