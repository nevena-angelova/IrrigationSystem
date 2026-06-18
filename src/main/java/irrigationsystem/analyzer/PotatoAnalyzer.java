package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.SensorValues;

public class PotatoAnalyzer extends Analyzer {

    private static final double CRITICAL_SOIL_MOISTURE = 35;
    private static final double MIN_TEMPERATURE = 7;
    private static final double MAX_TEMPERATURE = 30;

    public PotatoAnalyzer(Long plantId, SensorValues sensorValues, GrowthPhase growthPhase
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
            addReportWarning("warning.potato.soil.critical.low");
        }

        if (temperature < MIN_TEMPERATURE) {
            addReportWarning("warning.potato.temp.low");
        } else if (temperature > MAX_TEMPERATURE) {
            addReportWarning("warning.potato.temp.high");
        }

        if (temperature >= 15 && temperature <= 25 && humidity > 85 && soil > 75) {
            addReportWarning("warning.potato.mildew");
        }

        if (temperature > 25 && humidity > 80) {
            addReportWarning("warning.potato.alternaria");
        }

        if (temperature >= 10 && temperature <= 20 && soil > 85) {
            addReportWarning("warning.potato.gray.mold");
        }

        if (humidity < 40 && temperature > 28 && soil < 50) {
            addReportWarning("warning.potato.heat.dry");
        }

        if (humidity > 90 && temperature < 15) {
            addReportWarning("warning.potato.bacterial.rot");
        }

        return report;
    }

    @Override
    protected String getSoilTooWetWarning() {
        return "warning.potato.soil.too.wet";
    }

    @Override
    protected String getHighLightWarning() {
        return "warning.potato.high.light";
    }
}
