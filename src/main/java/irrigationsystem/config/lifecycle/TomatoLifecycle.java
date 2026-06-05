package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

public class TomatoLifecycle extends Lifecycle {

    public TomatoLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
            new GrowthPhase(
                0,
                29,
                getMessage("phase.tomato.initial.name"),
                getMessage("phase.tomato.initial.description"),
                65.0,
                80.0,
                plantType,
                4
            ),
            new GrowthPhase(
                30,
                69,
                getMessage("phase.tomato.development.name"),
                getMessage("phase.tomato.development.description"),
                60.0,
                75.0,
                plantType,
                6
            ),
            new GrowthPhase(
                70,
                119,
                getMessage("phase.tomato.midseason.name"),
                getMessage("phase.tomato.midseason.description"),
                65.0,
                75.0,
                plantType,
                8
            ),
            new GrowthPhase(
                120,
                149,
                getMessage("phase.tomato.late.name"),
                getMessage("phase.tomato.late.description"),
                55.0,
                65.0,
                plantType,
                5
            ),
            new GrowthPhase(
                150,
                Integer.MAX_VALUE,
                getMessage("phase.tomato.unknown.name"),
                getMessage("phase.tomato.unknown.description"),
                60.0,
                60.0,
                plantType,
                3
            )
        );
    }
}
