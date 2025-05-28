package irrigationsystem.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MeasureType> getMeasureTypes() {
        return measureTypes;
    }

    public void addMeasureType(MeasureType measureType) {
        this.measureTypes.add(measureType);
        measureType.getSensorTypes().add(this);
    }

    public void setMeasureTypes(List<MeasureType> measureTypes) {
        this.measureTypes = measureTypes;
    }
}
