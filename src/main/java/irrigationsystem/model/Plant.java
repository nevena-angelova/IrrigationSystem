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

    @Column(name = "planting_date", nullable = false)
    private LocalDate plantingDate;

    @Column(name = "plant_type_id")
    private int plantTypeId;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false, insertable = false, updatable = false)
    private PlantType plantType;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors = new ArrayList<>();

    @Column(name = "relay_number")
    private int relayNumber;

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

    public void setPlantTypeId(int plantTypeId) {
        this.plantTypeId = plantTypeId;
    }

    public int getRelayNumber() {
        return relayNumber;
    }

    public void setRelayNumber(int relayNumber) {
        this.relayNumber = relayNumber;
    }
}
