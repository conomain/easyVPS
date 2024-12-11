package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String role;


    @ManyToMany(targetEntity = Configuration.class)
    @JoinTable(
            name = "users_configurations",
            joinColumns = @JoinColumn(name = "id_configuration"), // another table
            inverseJoinColumns = @JoinColumn(name = "id_user") // this table
    )
    private List<User> configurations;


    @Override
    public Long getId() {
        return id;
    }
}
