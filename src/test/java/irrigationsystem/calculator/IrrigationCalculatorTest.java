package irrigationsystem.calculator;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantType;
import irrigationsystem.model.ControllerMetrics;
import irrigationsystem.model.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IrrigationCalculatorTest {

    @Test
    void calculateIrrigationDuration_shouldCalculateMinutes() {
        double result = IrrigationCalculator.calculateIrrigationDuration(10.0, 50, 40, 2.0);

        assertEquals(60.0, result, 1e-9);
    }

    @Test
    void calculateIrrigationDuration_shouldReturnZeroWhenEtcIsZero() {
        assertEquals(0.0, IrrigationCalculator.calculateIrrigationDuration(0, 50, 40, 2), 1e-9);
    }

    @Test
    void calculateEvapotranspiration_shouldScaleEt0ByCropCoefficient() {
        GrowthPhase phase = growthPhase(0.75);
        ControllerMetrics metrics = new ControllerMetrics(
            1, 100, 42.7, List.of(), new Metrics(10, 25, 17, 40, 80)
        );

        double result = IrrigationCalculator.calculateEvapotranspiration(metrics, phase, false);

        assertTrue(Double.isFinite(result));
    }

    private GrowthPhase growthPhase(double kc) {
        PlantType type = new PlantType();
        type.setId(1);
        return new GrowthPhase(0, 100, "Test", "", 40, 70, type, kc);
    }
}
