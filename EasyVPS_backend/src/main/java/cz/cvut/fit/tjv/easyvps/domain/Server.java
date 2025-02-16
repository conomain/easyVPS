package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "server")
@Getter
@Setter
public class Server implements EntityWithId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_server")
    private Long id;

    @Column(name = "server_cpu_cores")
    private Long cpu_cores;

    @Column(name = "server_ram")
    private Long ram;

    @Column(name = "server_storage")
    private Long storage;


    @OneToMany(mappedBy = "server", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instance> instances = new ArrayList<>();


    @Override
    public Long getId() {
        return id;
    }
}
