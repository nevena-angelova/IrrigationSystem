package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

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

    public Analyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        this.measureValues = measureValues;
        this.growthPhase = growthPhase;
        this.report = new ReportDto(plantId,
                measureValues.get(MeasureTypeEnum.SoilMoisture),
                growthPhase
        );
    }

    protected ReportDto getReport() {
        return report;
    }

    protected double getSoilMoisture() { return this.measureValues.get(MeasureTypeEnum.SoilMoisture); }

    protected double getMinSoilMoisture() {
        return growthPhase.getMinSoilMoisture();
    }

    protected double getMaxSoilMoisture() {
        return growthPhase.getMaxSoilMoisture();
    }

    protected double getTemperature() {
        return this.measureValues.get(MeasureTypeEnum.Temperature);
    }

    public abstract ReportDto analyze();

    protected void setReportNeedsIrrigation(boolean needsIrrigation) {
        this.report.setNeedsIrrigation(needsIrrigation);
    }

    protected void addReportWarning(String warning) {
        this.report.addWarning(warning);
    }
}
