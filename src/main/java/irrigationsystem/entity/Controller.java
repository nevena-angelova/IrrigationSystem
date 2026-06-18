package irrigationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "controllers")
public class Controller {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(unique = true, nullable = false)
    private int number;

    @Setter
    @Column(nullable = false)
    private double latitude;

    @Setter
    @Column(nullable = false)
    private double altitude;

    @Getter
    @OneToMany(mappedBy = "controller", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Sensor> sensors = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
