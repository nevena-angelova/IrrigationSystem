package irrigationsystem.analyzer;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;
import irrigationsystem.model.PlantTypeEnum;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class AnalyzerFactory {

    public static Analyzer createAnalyzer(Long PlantId, PlantType plantType, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> values,  boolean hasWater) {

        PlantTypeEnum plantTypeEnum = PlantTypeEnum.getValue(plantType.getId());

        switch (plantTypeEnum ) {
            case Tomato:
                return new TomatoAnalyzer(PlantId, growthPhase, values, hasWater);
            case Strawberry:
                return new StrawberryAnalyzer(PlantId, growthPhase, values, hasWater);
            case Potato:
                return new PotatoAnalyzer(PlantId, growthPhase, values, hasWater);
            case Carrot:
                return new CarrotAnalyzer(PlantId, growthPhase, values,  hasWater);
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + plantType);
        }

    }
}
