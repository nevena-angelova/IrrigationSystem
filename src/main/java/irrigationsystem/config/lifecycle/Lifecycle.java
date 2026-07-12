package irrigationsystem.config.lifecycle;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantType;
import lombok.Getter;

import java.util.List;
import java.util.ResourceBundle;

/*
Base class that represents plant lifecycle.
The lifecycle consists of several phases.
*/

@Getter
public abstract class Lifecycle {

    protected final PlantType plantType;
    private final ResourceBundle messages = ResourceBundle.getBundle("messages_bg");

    protected Lifecycle(PlantType plantType) {
        this.plantType = plantType;
    }

    protected String getMessage(String key) {
        return messages.getString(key);
    }

    protected abstract List<GrowthPhase> getPhases();

}
