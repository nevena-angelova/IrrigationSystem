package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

public class PotatoLifecycle extends Lifecycle {

    public PotatoLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
            new GrowthPhase(
                0,
                24,
                getMessage("phase.potato.initial.name"),
                getMessage("phase.potato.initial.description"),
                70.0,
                85.0,
                plantType,
                5
            ),
            new GrowthPhase(
                25,
                54,
                getMessage("phase.potato.development.name"),
                getMessage("phase.potato.development.description"),
                65.0,
                80.0,
                plantType,
                7
            ),
            new GrowthPhase(
                55,
                99,
                getMessage("phase.potato.midseason.name"),
                getMessage("phase.potato.midseason.description"),
                75.0,
                85.0,
                plantType,
                8
            ),
            new GrowthPhase(
                100,
                129,
                getMessage("phase.potato.late.name"),
                getMessage("phase.potato.late.description"),
                65.0,
                75.0,
                plantType,
                5
            ),
            new GrowthPhase(
                130,
                Integer.MAX_VALUE,
                getMessage("phase.potato.unknown.name"),
                getMessage("phase.potato.unknown.description"),
                60.0,
                60.0,
                plantType,
                3
            )
        );
    }
}
