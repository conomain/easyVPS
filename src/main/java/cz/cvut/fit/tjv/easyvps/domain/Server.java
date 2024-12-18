package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    private String ip;

    @Column(name = "server_cpu_cores")
    private Long cpu_cores;

    @Column(name = "server_ram")
    private Long ram;

    @Column(name = "server_storage")
    private Long storage;

    @Column(name = "is_running")
    private boolean isRunning;


    @ManyToMany(targetEntity = Configuration.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "server_configuration",
            joinColumns = @JoinColumn(name = "id_server"), // this table
            inverseJoinColumns = @JoinColumn(name = "id_configuration") // another table
    )
    private Set<Configuration> configurations = new HashSet<>();


    @Override
    public Long getId() {
        return id;
    }
}
