package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "configurations")
@Getter
@Setter
public class Configuration implements EntityWithId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_configuration")
    private Long id;

    private String name;

    private Long cpu_cores;

    private Long ram;

    private Long storage;

    private Long price;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Server.class)
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToMany(mappedBy = "configurations")
    private List<User> users;


    @Override
    public Long getId() {
        return id;
    }
}
