package irrigationsystem.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "device", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Sensor> sensors = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Relay> relays = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public List<Relay> getRelays() {
        return relays;
    }
}
