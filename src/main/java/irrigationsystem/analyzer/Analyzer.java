package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.SensorValues;
import lombok.Getter;

import java.util.Locale;
import java.util.ResourceBundle;

@Getter
public abstract class Analyzer {

    protected final SensorValues sensorValues;
    protected final GrowthPhase growthPhase;
    protected final ReportDto report;
    private final ResourceBundle messages = ResourceBundle.getBundle("messages", new Locale("bg"));


    protected Analyzer(
        Long plantId,
        SensorValues sensorValues,
        GrowthPhase growthPhase
    ) {
        this.sensorValues = sensorValues;
        this.growthPhase = growthPhase;
        this.report = new ReportDto(plantId, growthPhase);
    }

    protected void analyzeCommonRules() {

        report.setTemperature(sensorValues.getTemperature());
        report.setHumidity(sensorValues.getHumidity());
        report.setSoilMoisture(sensorValues.getSoilMoisture());
        report.setLight(sensorValues.getLight());

        if (sensorValues.getSoilMoisture() > growthPhase.getMaxSoilMoisture()) {
            addReportWarning(getSoilTooWetWarning());
        }

        if (sensorValues.getLight() > getHighLightThreshold()) {
            addReportWarning(getHighLightWarning());
        }
    }

    protected abstract String getSoilTooWetWarning();

    protected abstract String getHighLightWarning();

    protected double getHighLightThreshold() {
        return 90000;
    }

    protected void addReportWarning(String warning) {
        this.report.addWarning(messages.getString(warning));
    }

    public abstract ReportDto analyze();
}
