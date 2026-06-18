package irrigationsystem.config.lifecycle;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantType;

import java.util.List;

public class CarrotLifecycle extends Lifecycle {

    public CarrotLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
            new GrowthPhase(
                0,
                29,
                getMessage("phase.carrot.initial.name"),
                getMessage("phase.carrot.initial.description"),
                70.0,
                85.0,
                plantType,
                0.70
            ),
            new GrowthPhase(
                30,
                69,
                getMessage("phase.carrot.development.name"),
                getMessage("phase.carrot.development.description"),
                65.0,
                80.0,
                plantType,
                0.88
            ),
            new GrowthPhase(
                70,
                129,
                getMessage("phase.carrot.midseason.name"),
                getMessage("phase.carrot.midseason.description"),
                65.0,
                75.0,
                plantType,
                1.05
            ),
            new GrowthPhase(
                130,
                149,
                getMessage("phase.carrot.late.name"),
                getMessage("phase.carrot.late.description"),
                60.0,
                70.0,
                plantType,
                0.95
            ),
            new GrowthPhase(
                150,
                Integer.MAX_VALUE,
                getMessage("phase.carrot.unknown.name"),
                getMessage("phase.carrot.unknown.description"),
                60.0,
                60.0,
                plantType,
                0.95
            )
        );
    }
}
