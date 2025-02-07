package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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

    @Column(name = "configuration_name")
    private String name;

    @Column(name = "configuration_cpu_cores")
    private Long cpu_cores;

    @Column(name = "configuration_ram")
    private Long ram;

    @Column(name = "configuration_storage")
    private Long storage;

    @Column(name = "configuration_price")
    private Long price;


    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Instance> instances = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }
}
