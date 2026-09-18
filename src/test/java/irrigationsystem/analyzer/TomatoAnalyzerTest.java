package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TomatoAnalyzerTest {
    @Test
    void analyze_shouldCopySensorValuesToReport() {
        ReportDto r = new TomatoAnalyzer(10L, AnalyzerTestSupport.values(22, 55, 60, 50000), AnalyzerTestSupport.phase()).analyze();
        assertEquals(10L, r.getPlantId());
        assertEquals(22, r.getTemperature());
        assertEquals(55, r.getSoilMoisture());
        assertEquals(60, r.getHumidity());
        assertEquals(50000, r.getLight());
        assertTrue(r.getWarnings().isEmpty());
    }

    @Test
    void analyze_shouldAddWarningsForCriticalConditions() {
        ReportDto r = new TomatoAnalyzer(10L, AnalyzerTestSupport.values(10, 30, 95, 120000), AnalyzerTestSupport.phase()).analyze();
        assertTrue(r.getWarnings().contains("warning.tomato.soil.critical.low"));
        assertTrue(r.getWarnings().contains("warning.tomato.temp.low"));
        assertTrue(r.getWarnings().contains("warning.tomato.high.light"));
    }
}
