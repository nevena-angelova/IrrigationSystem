package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;
import java.util.Optional;

/*
A base class used to analyze sensor measure values
Each successor implements its own logic for the method analyze()
The class has a reference to GrowthPhaseInfo that keeps information about plant lifecycle
that is used while analyzing data
 */

public abstract class Analyzer {
    private final Map<MeasureTypeEnum, Double> measureValues;
    private final GrowthPhase growthPhase;
    private ReportDto report;

    public Analyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        this.measureValues = measureValues;
        this.growthPhase = growthPhase;
        this.report = new ReportDto(PlantId,
                measureValues.get(MeasureTypeEnum.Humidity),
                growthPhase
        );
    }

    protected ReportDto getReport() {
        return report;
    }

    protected double getMinHumidity() {
        return growthPhase.getMinHumidity();
    }

    protected double getMaxHumidity() {
        return growthPhase.getMaxHumidity();
    }

    protected double getTemperature() {
        return this.measureValues.get(MeasureTypeEnum.Temperature);
    }

    protected double getHumidity() {
        return this.measureValues.get(MeasureTypeEnum.Humidity);
    }

    protected Optional<Double> getPressure() {
        if (this.measureValues == null) return Optional.empty();
        return Optional.ofNullable(this.measureValues.get(MeasureTypeEnum.Pressure));
    }

    public abstract ReportDto analyze();

    protected void setReportNeedsIrrigation(boolean needsIrrigation) {
        this.report.setNeedsIrrigation(needsIrrigation);
    }

    protected void addReportWarning(String warning) {
        this.report.addWarning(warning);
    }
}
