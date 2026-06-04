package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;
import lombok.Getter;

import java.util.List;

/*

Base class that represents plant lifecycle.
The lifecycle consists of several phases.

*/

@Getter
public abstract class Lifecycle {

    protected final PlantType plantType;

    protected Lifecycle(PlantType plantType) {
        this.plantType = plantType;
    }

    protected abstract List<GrowthPhase> getPhases();

}
