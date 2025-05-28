package irrigationsystem.sensor.analizer;

import irrigationsystem.model.CropTypeEnum;
import irrigationsystem.model.MeasureTypeEnum;
import irrigationsystem.sensor.lifecycle.CarrotLifecycle;
import irrigationsystem.sensor.lifecycle.PotatoLifecycle;
import irrigationsystem.sensor.lifecycle.StrawberryLifecycle;
import irrigationsystem.sensor.lifecycle.TomatoLifecycle;

import java.time.LocalDate;
import java.util.Map;

public class AnalyzerFactory {

    public static Analyzer CreateAnalyzer(Long cropId, LocalDate plantingDate, CropTypeEnum  cropType, Map<MeasureTypeEnum, Double> values){

        switch (cropType) {
            case Tomato:
                return new TomatoAnalyzer(cropId, plantingDate, new TomatoLifecycle(), values);
            case Strawberry:
                return new StrawberryAnalyzer(cropId, plantingDate, new StrawberryLifecycle(), values);
            case Potato:
                return new PotatoAnalyzer(cropId, plantingDate, new PotatoLifecycle(), values);
            case Carrot:
                return new CarrotAnalyzer(cropId, plantingDate, new CarrotLifecycle(), values);
            default:
                throw new IllegalArgumentException("Unknown sensor type: " + cropType);
        }

    }
}
