package irrigationsystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="planting_date", nullable = false)
    private LocalDate plantingDate;

    @Column(name = "crop_type_id", nullable = false)
    private Long cropTypeId;

    @ManyToOne
    @JoinColumn(name = "crop_type_id", nullable = false, insertable = false, updatable = false)
    private CropType cropType;

    @OneToMany
    @JoinColumn(name = "crop_id")
    private List<Sensor> sensors = new ArrayList<>();

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

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

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCropTypeId(long cropTypeId) {
        this.cropTypeId = cropTypeId;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
