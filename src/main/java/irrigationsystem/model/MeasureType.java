package irrigationsystem.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "measure_types")
public class MeasureType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String unit;

    @ManyToMany(mappedBy = "measureTypes")
    private List<SensorType> sensorTypes = new ArrayList<>();

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<SensorType> getSensorTypes() {
        return sensorTypes;
    }

    public void setSensorTypes(List<SensorType>  sensorTypes) {
        this.sensorTypes = sensorTypes;
    }
}
