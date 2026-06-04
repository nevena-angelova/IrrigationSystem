package irrigationsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Entity
@Table(name = "sensors")
public class Sensor {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Device device;

    @Getter
    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;
}
