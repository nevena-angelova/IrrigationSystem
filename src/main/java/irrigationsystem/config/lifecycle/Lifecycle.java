package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

/*

Base class that represents plant lifecycle.
The lifecycle consists of several phases.

*/

public abstract class Lifecycle {

    protected final PlantType plantType;

    public Lifecycle(PlantType plantType) {
        this.plantType = plantType;
    }

    protected abstract List<GrowthPhase> getPhases();

    public PlantType getPlantType(){
        return plantType;
    }
}
