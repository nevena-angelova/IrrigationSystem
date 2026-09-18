package irrigationsystem.calculator;

import irrigationsystem.model.ControllerMetrics;
import irrigationsystem.model.Metrics;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EvapotranspirationCalculatorTest {

    @Test
    void calculateET0_shouldReturnFiniteValue() {
        ControllerMetrics metrics = new ControllerMetrics(
            1, 100, 42.7, List.of(), new Metrics(10, 25, 17, 40, 80)
        );

        double result = new EvapotranspirationCalculator(metrics).calculateET0(180, false);

        assertTrue(Double.isFinite(result));
    }

    @Test
    void calculateETc_shouldEqualEt0TimesKc() {
        ControllerMetrics metrics = new ControllerMetrics(
            1, 100, 42.7, List.of(), new Metrics(10, 25, 17, 40, 80)
        );
        EvapotranspirationCalculator calculator = new EvapotranspirationCalculator(metrics);

        double et0 = calculator.calculateET0(180, false);
        double etc = calculator.calculateETc(180, 0.5, false);

        assertEquals(et0 * 0.5, etc, 1e-9);
    }
}
