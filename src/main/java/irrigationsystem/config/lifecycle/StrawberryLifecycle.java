package irrigationsystem.config.lifecycle;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantType;

import java.util.List;

public class StrawberryLifecycle extends Lifecycle {

    public StrawberryLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
            new GrowthPhase(
                0,
                19,
                getMessage("phase.strawberry.initial.name"),
                getMessage("phase.strawberry.initial.description"),
                70.0,
                85.0,
                plantType,
                0.40
            ),
            new GrowthPhase(
                20,
                49,
                getMessage("phase.strawberry.development.name"),
                getMessage("phase.strawberry.development.description"),
                65.0,
                80.0,
                plantType,
                0.63
            ),
            new GrowthPhase(
                50,
                109,
                getMessage("phase.strawberry.midseason.name"),
                getMessage("phase.strawberry.midseason.description"),
                60.0,
                75.0,
                plantType,
                0.85
            ),
            new GrowthPhase(
                110,
                139,
                getMessage("phase.strawberry.late.name"),
                getMessage("phase.strawberry.late.description"),
                55.0,
                65.0,
                plantType,
                0.75
            ),
            new GrowthPhase(
                140,
                Integer.MAX_VALUE,
                getMessage("phase.strawberry.unknown.name"),
                getMessage("phase.strawberry.unknown.description"),
                60.0,
                60.0,
                plantType,
                0.75
            )
        );
    }
}
