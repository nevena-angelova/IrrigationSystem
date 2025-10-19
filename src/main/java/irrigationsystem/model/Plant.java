package irrigationsystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="planting_date", nullable = false)
    private LocalDate plantingDate;

    @Column(name = "plant_type_id")
    private Long plantTypeId;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false, insertable = false, updatable = false)
    private PlantType plantType;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "relay_id")
    private Relay relay;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setPlantTypeId(Long PlantTypeId) {
        this.plantTypeId = PlantTypeId;
    }

    public void setRelay(Relay relay) {
        this.relay = relay;
    }
}
