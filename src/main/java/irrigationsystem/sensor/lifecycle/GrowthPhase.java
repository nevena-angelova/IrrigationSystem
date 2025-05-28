package irrigationsystem.sensor.lifecycle;

public class GrowthPhase {
    private final int startDay;
    private final int endDay;
    private final GrowthPhaseInfo info;

    public GrowthPhase(int startDay, int endDay, GrowthPhaseInfo info) {
        this.startDay = startDay;
        this.endDay = endDay;
        this.info = info;
    }

    public boolean isInRange(long day) {
        return day >= startDay && day <= endDay;
    }

    public GrowthPhaseInfo getInfo() {
        return info;
    }
}
