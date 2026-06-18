package irrigationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planting_date", nullable = false)
    private LocalDate plantingDate;

    @Column(name = "plant_type_id")
    private int plantTypeId;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false, insertable = false, updatable = false)
    private PlantType plantType;

    @Column(name = "distance_x")
    private int distanceX;

    @Column(name = "distance_y")
    private int distanceY;

    @Column(name = "emitter_flow")
    private double emitterFlow;

    @Column(name = "etc")
    private double etc;

    @Column(name = "area_number")
    private int areaNumber;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "plant_sensors",
        joinColumns = @JoinColumn(name = "plant_id"),
        inverseJoinColumns = @JoinColumn(name = "sensor_id")
    )
    private List<Sensor> sensors = new ArrayList<>();

    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.getPlants().add(this);
    }
}
