package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrawberryAnalyzerTest {
    @Test
    void analyze_shouldUseLowerHighLightThreshold() {
        ReportDto r = new StrawberryAnalyzer(1L, AnalyzerTestSupport.values(20, 60, 60, 90000), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.strawberry.high.light"));
    }

    @Test
    void analyze_shouldDetectBotrytisAndFruitMold() {
        ReportDto r = new StrawberryAnalyzer(1L, AnalyzerTestSupport.values(20, 90, 95, 1000), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.strawberry.botrytis"));
        assertTrue(r.getWarnings().contains("warning.strawberry.fruit.mold"));
    }
}
