package irrigationsystem.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Getter;

@Entity
@Table(name = "growth_phases")
public class GrowthPhase {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Column(name="start_day", nullable = false)
    private int startDay;

    @Getter
    @Column(name="end_day", nullable = false)
    private int endDay;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column
    private String details;

    @Getter
    @Column(nullable = false)
    private double minSoilMoisture;

    @Getter
    @Column(nullable = false)
    private double maxSoilMoisture;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false)
    private PlantType plantType;

    @Getter
    @Column(name="crop_coefficient", nullable = false)
    private double cropCoefficient;


    public GrowthPhase() {
    }

    public GrowthPhase(int startDay, int endDay, String name, String details, double minSoilMoisture, double maxSoilMoisture, PlantType plantType, double cropCoefficient) {
        this.name = name;
        this.details = details;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.startDay = startDay;
        this.endDay = endDay;
        this.plantType = plantType;
        this.cropCoefficient = cropCoefficient;
    }

  public Integer getPlantTypeId() {
        return plantType.getId();
    }
}
