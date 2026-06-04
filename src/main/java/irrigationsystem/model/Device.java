package irrigationsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(unique = true, nullable = false)
    private String name;

    @Getter
    @OneToMany(mappedBy = "device", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Sensor> sensors = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
