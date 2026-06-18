package irrigationsystem.analyzer;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantTypeEnum;
import irrigationsystem.model.SensorValues;

public class AnalyzerFactory {
    private AnalyzerFactory() {
        /* This utility class should not be instantiated */
    }

    public static Analyzer createAnalyzer(Long plantId, SensorValues sensorValues, Integer plantTypeId, GrowthPhase growthPhase) {

        PlantTypeEnum plantTypeEnum = PlantTypeEnum.getValue(plantTypeId);

        return switch (plantTypeEnum) {
            case Tomato -> new TomatoAnalyzer(plantId, sensorValues, growthPhase);
            case Strawberry -> new StrawberryAnalyzer(plantId, sensorValues, growthPhase);
            case Potato -> new PotatoAnalyzer(plantId, sensorValues, growthPhase);
            case Carrot -> new CarrotAnalyzer(plantId, sensorValues, growthPhase);
            default -> throw new IllegalArgumentException("Unknown sensor type: " + plantTypeEnum);
        };
    }
}
