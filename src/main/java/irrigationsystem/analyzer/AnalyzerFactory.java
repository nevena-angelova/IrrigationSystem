package irrigationsystem.analyzer;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;
import irrigationsystem.model.PlantTypeEnum;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class AnalyzerFactory {
    private AnalyzerFactory() {
        /* This utility class should not be instantiated */
    }


    public static Analyzer createAnalyzer(Long plantId, PlantType plantType, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> values) {

        PlantTypeEnum plantTypeEnum = PlantTypeEnum.getValue(plantType.getId());

        return switch (plantTypeEnum) {
            case Tomato -> new TomatoAnalyzer(plantId, growthPhase, values);
            case Strawberry -> new StrawberryAnalyzer(plantId, growthPhase, values);
            case Potato -> new PotatoAnalyzer(plantId, growthPhase, values);
            case Carrot -> new CarrotAnalyzer(plantId, growthPhase, values);
            default -> throw new IllegalArgumentException("Unknown sensor type: " + plantType);
        };
    }
}
