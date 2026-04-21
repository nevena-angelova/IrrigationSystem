package irrigationsystem.analyzer;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;
import irrigationsystem.model.PlantTypeEnum;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class AnalyzerFactory {

    public static Analyzer createAnalyzer(Long plantId, PlantType plantType, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> values, boolean hasWater) {

        PlantTypeEnum plantTypeEnum = PlantTypeEnum.getValue(plantType.getId());

        switch (plantTypeEnum ) {
            case Tomato:
                return new TomatoAnalyzer(plantId, growthPhase, values, hasWater);
            case Strawberry:
                return new StrawberryAnalyzer(plantId, growthPhase, values, hasWater);
            case Potato:
                return new PotatoAnalyzer(plantId, growthPhase, values, hasWater);
            case Carrot:
                return new CarrotAnalyzer(plantId, growthPhase, values,  hasWater);
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + plantType);
        }

    }
}
