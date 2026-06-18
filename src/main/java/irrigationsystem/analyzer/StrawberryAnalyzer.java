package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.SensorValues;

public class StrawberryAnalyzer extends Analyzer {

    private static final double CRITICAL_SOIL_MOISTURE = 40;

    private static final double MIN_TEMPERATURE = 5;
    private static final double MAX_TEMPERATURE = 30;

    private static final double HIGH_HUMIDITY = 85;
    private static final double VERY_HIGH_HUMIDITY = 90;

    private static final double LOW_HUMIDITY = 40;

    public StrawberryAnalyzer(
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
            addReportWarning("warning.strawberry.soil.critical.low");
        }

        if (temperature < MIN_TEMPERATURE) {
            addReportWarning("warning.strawberry.temp.low");
        } else if (temperature > MAX_TEMPERATURE) {
            addReportWarning("warning.strawberry.temp.high");
        }

        if (humidity > HIGH_HUMIDITY && temperature >= 15 && temperature <= 25) {
            addReportWarning("warning.strawberry.botrytis");
        }

        if (humidity > 80 && temperature > 25) {
            addReportWarning("warning.strawberry.powdery.mildew");
        }

        if (humidity < LOW_HUMIDITY && temperature > 28) {
            addReportWarning("warning.strawberry.hot.dry");
        }

        if (temperature < 12 && humidity > VERY_HIGH_HUMIDITY && soil > 80) {
            addReportWarning("warning.strawberry.root.rot");
        }

        if (soil > 80 && humidity > VERY_HIGH_HUMIDITY) {
            addReportWarning("warning.strawberry.fruit.mold");
        }

        if (soil < 50 && humidity < LOW_HUMIDITY && temperature > 30) {
            addReportWarning("warning.strawberry.heat.stress");
        }

        return report;
    }

    @Override
    protected String getSoilTooWetWarning() {
        return "warning.strawberry.soil.too.wet";
    }

    @Override
    protected String getHighLightWarning() {
        return "warning.strawberry.high.light";
    }

    @Override
    protected double getHighLightThreshold() {
        return 80000;
    }
}
