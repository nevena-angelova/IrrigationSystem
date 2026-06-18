package irrigationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Double value;

    @Setter
    @Getter
    @Column(name = "creation_date", nullable = false)
    OffsetDateTime creationDate;

    @Setter
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "measure_type_id")
    private MeasureType measureType;

    public void setTimestamp(OffsetDateTime createdAt) {
        this.creationDate = createdAt;
    }

}
