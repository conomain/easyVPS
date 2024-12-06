package cz.cvut.fit.tjv.easyvps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    private String status;


    @OneToMany(fetch = FetchType.LAZY, targetEntity = UserConfiguration.class, mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserConfiguration> user_configurations;


    @Override
    public Long getId() {
        return id;
    }
}
