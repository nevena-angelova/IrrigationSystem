package irrigationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "controller_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Controller controller;

    @Getter
    @ManyToMany(mappedBy = "sensors")
    private List<Plant> plants = new ArrayList<>();
}
