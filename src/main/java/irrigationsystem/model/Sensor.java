package irrigationsystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "sensors")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Plant getPlant() {
        return plant;
    }
}
