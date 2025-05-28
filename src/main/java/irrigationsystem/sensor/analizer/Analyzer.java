package irrigationsystem.sensor.analizer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.MeasureTypeEnum;
import irrigationsystem.sensor.lifecycle.GrowthPhaseInfo;
import irrigationsystem.sensor.lifecycle.Lifecycle;

import java.time.LocalDate;
import java.util.Map;

public abstract class Analyzer {
    private final Map<MeasureTypeEnum, Double> measureValues;
    private final GrowthPhaseInfo growthPhaseInfo;
    private ReportDto report;

    public Analyzer(Long cropId, LocalDate plantingDate, Lifecycle lifecycle, Map<MeasureTypeEnum, Double> measureValues) {
        this.measureValues = measureValues;
        this.growthPhaseInfo = lifecycle.getGrowthPhase(plantingDate);
        this.report = new ReportDto(cropId,
                measureValues.get(MeasureTypeEnum.Humidity),
                growthPhaseInfo.getMinHumidity(),
                growthPhaseInfo.getMaxHumidity()
        );
    }

    protected ReportDto getReport() {
        return report;
    }

    protected double getMinHumidity() {
        return growthPhaseInfo.getMinHumidity();
    }

    protected double getMaxHumidity() {
        return growthPhaseInfo.getMaxHumidity();
    }

    protected double getTemperature() {
        return this.measureValues.get(MeasureTypeEnum.Temperature);
    }

    protected double getHumidity() {
        return this.measureValues.get(MeasureTypeEnum.Humidity);
    }

    protected double getPressure() {
        return this.measureValues.get(MeasureTypeEnum.Pressure);
    }

    protected GrowthPhaseInfo getGrowthPhaseInfo() {
        return this.growthPhaseInfo;
    }

    public abstract ReportDto analyze();

    protected void setReportNeedsIrrigation(boolean needsIrrigation) {
        this.report.setNeedsIrrigation(needsIrrigation);
    }

    protected void addReportWarning(String warning) {
        this.report.addWarning(warning);
    }
}
