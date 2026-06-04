package irrigationsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plants")
public class Plant {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "planting_date", nullable = false)
    private LocalDate plantingDate;

    @Setter
    @Column(name = "plant_type_id")
    private int plantTypeId;

    @Getter
    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false, insertable = false, updatable = false)
    private PlantType plantType;

    @Getter
    @Setter
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors = new ArrayList<>();

    @Setter
    @Getter
    @Column(name = "relay_number")
    private int relayNumber;
}
