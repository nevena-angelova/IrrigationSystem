package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.SensorValues;

public class TomatoAnalyzer extends Analyzer {

    private static final double CRITICAL_SOIL_MOISTURE = 40;

    private static final double MIN_TEMPERATURE = 12;
    private static final double MAX_TEMPERATURE = 32;

    private static final double LOW_HUMIDITY = 40;
    private static final double VERY_LOW_HUMIDITY = 35;

    private static final double HIGH_HUMIDITY = 80;
    private static final double VERY_HIGH_HUMIDITY = 85;

    public TomatoAnalyzer(Long plantId, SensorValues sensorValues, GrowthPhase growthPhase) {
        super(plantId, sensorValues, growthPhase);
    }

    @Override
    public ReportDto analyze() {

        analyzeCommonRules();

        double soil = sensorValues.getSoilMoisture();
        double temperature = sensorValues.getTemperature();
        double humidity = sensorValues.getHumidity();

        if (soil < CRITICAL_SOIL_MOISTURE) {
            addReportWarning("warning.tomato.soil.critical.low");
        }

        if (temperature < MIN_TEMPERATURE) {
            addReportWarning("warning.tomato.temp.low");
        } else if (temperature > MAX_TEMPERATURE) {
            addReportWarning("warning.tomato.temp.high");
        }

        if (humidity > VERY_HIGH_HUMIDITY && temperature >= 18 && temperature <= 25) {
            addReportWarning("warning.tomato.mildew");
        }

        if (humidity > HIGH_HUMIDITY && temperature > 26) {
            addReportWarning("warning.tomato.fungal.infections");
        }

        if (humidity < LOW_HUMIDITY && temperature > 30) {
            addReportWarning("warning.tomato.heat.dry.stress");
        }

        if (soil > 80 && humidity > VERY_HIGH_HUMIDITY) {
            addReportWarning("warning.tomato.moisture.excess");
        }

        if (soil < 50 && humidity < LOW_HUMIDITY && temperature > 30) {
            addReportWarning("warning.tomato.fruit.deformation");
        }

        if (temperature > 35 && humidity < VERY_LOW_HUMIDITY) {
            addReportWarning("warning.tomato.heat.shock");
        }

        if (temperature < 12 && humidity > HIGH_HUMIDITY && soil > 75) {
            addReportWarning("warning.tomato.bacterial.diseases");
        }

        return report;
    }

    @Override
    protected String getSoilTooWetWarning() {
        return "warning.tomato.soil.too.wet";
    }

    @Override
    protected String getHighLightWarning() {
        return "warning.tomato.high.light";
    }

    @Override
    protected double getHighLightThreshold() {
        return 100000;
    }
}
