package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/*
A base class used to analyze sensor measure values
Each successor implements its own logic for the method analyze()
The class has a reference to GrowthPhaseInfo that keeps information about plant lifecycle
that is used while analyzing data
 */

public abstract class Analyzer {
    private final Map<MeasureTypeEnum, Double> measureValues;
    private final GrowthPhase growthPhase;
    private final ReportDto report;
    private final double temperature;
    private final double soilMoisture;
    private final double humidity;
    private final double light;
    private final ResourceBundle messages = ResourceBundle.getBundle("messages", new Locale("bg"));

    protected Analyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        this.measureValues = measureValues;
        this.growthPhase = growthPhase;
        this.report = new ReportDto(plantId, growthPhase);
        this.temperature = this.measureValues.get(MeasureTypeEnum.Temperature);
        this.soilMoisture = this.measureValues.get(MeasureTypeEnum.SoilMoisture);
        this.humidity = this.measureValues.get(MeasureTypeEnum.Humidity);
        this.light = this.measureValues.get(MeasureTypeEnum.Light);
    }

    protected ReportDto getReport() {
        return report;
    }

    protected double getSoilMoisture() {
        return this.soilMoisture;
    }

    protected double getTemperature() {
        return this.temperature;
    }

    protected double getHumidity() {
        return this.humidity;
    }

    protected double getLight() {
        return this.light;
    }

    protected double getMinSoilMoisture() {
        return growthPhase.getMinSoilMoisture();
    }

    protected double getMaxSoilMoisture() {
        return growthPhase.getMaxSoilMoisture();
    }

    public abstract ReportDto analyze();

    protected void setReportNeedsIrrigation(boolean needsIrrigation) {
        this.report.setNeedsIrrigation(needsIrrigation);
    }

    protected void addReportWarning(String warning) {
        this.report.addWarning(messages.getString(warning));
    }
}
