package irrigationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "sensor_types")
public class SensorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "sensor_measure_types",
        joinColumns = @JoinColumn(name = "sensor_type_id"),
        inverseJoinColumns = @JoinColumn(name = "measure_type_id")
    )
    private List<MeasureType> measureTypes = new ArrayList<>();

    public void addMeasureType(MeasureType measureType) {
        this.measureTypes.add(measureType);
        measureType.getSensorTypes().add(this);
    }
}
