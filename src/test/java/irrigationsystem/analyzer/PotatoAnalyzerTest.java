package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PotatoAnalyzerTest {
    @Test
    void analyze_shouldDetectLowSoilAndLowTemperature() {
        ReportDto r = new PotatoAnalyzer(2L, AnalyzerTestSupport.values(5, 30, 50, 100), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.potato.soil.critical.low"));
        assertTrue(r.getWarnings().contains("warning.potato.temp.low"));
    }

    @Test
    void analyze_shouldDetectMildew() {
        ReportDto r = new PotatoAnalyzer(2L, AnalyzerTestSupport.values(20, 80, 90, 100), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.potato.mildew"));
    }
}
