package irrigationsystem.analyzer;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantType;
import irrigationsystem.model.SensorValues;

final class AnalyzerTestSupport {
    private AnalyzerTestSupport() {}

    static GrowthPhase phase() {
        PlantType type = new PlantType();
        type.setId(1);
        return new GrowthPhase(0, 100, "Vegetative", "details", 40, 70, type, 1.0);
    }

    static SensorValues values(double temperature, double soil, double humidity, double light) {
        SensorValues v = new SensorValues();
        v.setTemperature(temperature);
        v.setSoilMoisture(soil);
        v.setHumidity(humidity);
        v.setLight(light);
        return v;
    }
}
