package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Instance> servers = new HashSet<>();

    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserConfiguration> users = new HashSet<>();


    @Override
    public Long getId() {
        return id;
    }
}
