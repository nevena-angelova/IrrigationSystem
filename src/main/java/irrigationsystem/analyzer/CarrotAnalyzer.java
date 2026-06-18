package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.SensorValues;

public class CarrotAnalyzer extends Analyzer {

    private static final double CRITICAL_SOIL_MOISTURE = 35;
    private static final double MIN_TEMPERATURE = 5;
    private static final double MAX_TEMPERATURE = 28;
    private static final double MIN_HUMIDITY = 40;
    private static final double MAX_HUMIDITY = 85;

    public CarrotAnalyzer(
        Long plantId,
        SensorValues sensorValues,
        GrowthPhase growthPhase
    ) {
        super(plantId, sensorValues, growthPhase);
    }

    @Override
    public ReportDto analyze() {

        analyzeCommonRules();

        double soil = sensorValues.getSoilMoisture();
        double temperature = sensorValues.getTemperature();
        double humidity = sensorValues.getHumidity();

        if (soil < CRITICAL_SOIL_MOISTURE) {
            addReportWarning("warning.carrot.soil.critical.low");
        }

        if (temperature < MIN_TEMPERATURE) {
            addReportWarning("warning.carrot.temp.low");
        } else if (temperature > MAX_TEMPERATURE) {
            addReportWarning("warning.carrot.temp.high");
        }

        if (humidity < MIN_HUMIDITY) {
            addReportWarning("warning.carrot.humidity.low");
        } else if (humidity > MAX_HUMIDITY) {
            addReportWarning("warning.carrot.humidity.high");
        }

        if (humidity > 80 && soil > 80) {
            addReportWarning("warning.carrot.soil.air.high.moisture");
        }

        if (temperature > 25 && humidity > 80) {
            addReportWarning("warning.carrot.alternaria");
        }

        if (temperature >= 15 &&
            temperature <= 25 &&
            humidity > 85 &&
            soil > 80) {

            addReportWarning("warning.carrot.sclerotinia");
        }

        if (temperature > 20 && soil > 85) {
            addReportWarning("warning.carrot.root.rot");
        }

        return report;
    }

    @Override
    protected String getSoilTooWetWarning() {
        return "warning.carrot.soil.too.wet";
    }

    @Override
    protected String getHighLightWarning() {
        return "warning.carrot.high.light";
    }
}
