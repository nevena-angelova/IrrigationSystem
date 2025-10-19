package irrigationsystem.analyzer;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;
import irrigationsystem.model.PlantTypeEnum;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class AnalyzerFactory {

    public static Analyzer createAnalyzer(Long PlantId, PlantType plantType, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> values){

        PlantTypeEnum plantTypeEnum = PlantTypeEnum.getValue(plantType.getId());

        switch (plantTypeEnum ) {
            case Tomato:
                return new TomatoAnalyzer(PlantId, growthPhase, values);
            case Strawberry:
                return new StrawberryAnalyzer(PlantId, growthPhase, values);
            case Potato:
                return new PotatoAnalyzer(PlantId, growthPhase, values);
            case Carrot:
                return new CarrotAnalyzer(PlantId, growthPhase, values);
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + plantType);
        }

    }
}
