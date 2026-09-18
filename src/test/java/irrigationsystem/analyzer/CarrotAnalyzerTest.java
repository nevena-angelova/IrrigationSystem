package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarrotAnalyzerTest {
    @Test
    void analyze_shouldDetectCriticalSoilAndTemperature() {
        ReportDto r = new CarrotAnalyzer(3L, AnalyzerTestSupport.values(3, 30, 50, 100), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.carrot.soil.critical.low"));
        assertTrue(r.getWarnings().contains("warning.carrot.temp.low"));
    }

    @Test
    void analyze_shouldDetectAlternariaAndRootRot() {
        ReportDto r = new CarrotAnalyzer(3L, AnalyzerTestSupport.values(27, 90, 85, 100), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.carrot.alternaria"));
        assertTrue(r.getWarnings().contains("warning.carrot.root.rot"));
    }
}
