package irrigationsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "growth_phases")
public class GrowthPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="start_day", nullable = false)
    private int startDay;

    @Column(name="end_day", nullable = false)
    private int endDay;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String details;

    @Column(nullable = false)
    private double minSoilMoisture;

    @Column(nullable = false)
    private double maxSoilMoisture;

    @ManyToOne
    @JoinColumn(name = "plant_type_id", nullable = false)
    private PlantType plantType;

    @Column(name="irrigation_duration", nullable = false)
    private int irrigationDuration;


    public GrowthPhase() {
    }

    public GrowthPhase(int startDay, int endDay, String name, String details, double minSoilMoisture, double maxSoilMoisture, PlantType plantType, int irrigationDuration) {
        this.name = name;
        this.details = details;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.startDay = startDay;
        this.endDay = endDay;
        this.plantType = plantType;
        this.irrigationDuration = irrigationDuration;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public double getMinSoilMoisture() {
        return minSoilMoisture;
    }

    public double getMaxSoilMoisture() {
        return maxSoilMoisture;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public Integer getPlantTypeId() {
        return plantType.getId();
    }

    public int getIrrigationDuration() {
        return irrigationDuration;
    }

}
