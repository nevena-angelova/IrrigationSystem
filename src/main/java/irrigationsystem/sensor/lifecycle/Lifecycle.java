package irrigationsystem.sensor.lifecycle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
