package irrigationsystem.sensor.lifecycle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/*

Base class that represents plant lifecycle.
The lifecycle consists of several phases.
The current phase is determined by the crop planting date.

*/

public abstract class Lifecycle {

    protected abstract List<GrowthPhase> getPhases();

    public GrowthPhaseInfo getGrowthPhase(LocalDate plantingDate) {
        long days = ChronoUnit.DAYS.between(plantingDate, LocalDate.now());

        for (GrowthPhase phase : getPhases()) {
            if (phase.isInRange(days)) {
                return phase.getInfo();
            }
        }

        return getPhases().getLast().getInfo();
    }
}
