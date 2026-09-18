package irrigationsystem.analyzer;

import irrigationsystem.entity.PlantTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzerFactoryTest {
    @Test
    void createAnalyzer_shouldCreateCorrectAnalyzerForEachPlantType() {
        var values = AnalyzerTestSupport.values(20, 50, 60, 100);
        var phase = AnalyzerTestSupport.phase();

        assertInstanceOf(TomatoAnalyzer.class, AnalyzerFactory.createAnalyzer(1L, values, PlantTypeEnum.Tomato.getValue(), phase));
        assertInstanceOf(StrawberryAnalyzer.class, AnalyzerFactory.createAnalyzer(1L, values, PlantTypeEnum.Strawberry.getValue(), phase));
        assertInstanceOf(PotatoAnalyzer.class, AnalyzerFactory.createAnalyzer(1L, values, PlantTypeEnum.Potato.getValue(), phase));
        assertInstanceOf(CarrotAnalyzer.class, AnalyzerFactory.createAnalyzer(1L, values, PlantTypeEnum.Carrot.getValue(), phase));
    }

    @Test
    void createAnalyzer_shouldRejectUnknownPlantType() {
        assertThrows(IllegalArgumentException.class, () -> AnalyzerFactory.createAnalyzer(1L, AnalyzerTestSupport.values(20,50,60,100), 999, AnalyzerTestSupport.phase()));
    }
}
