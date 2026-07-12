package irrigationsystem.analyzer;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.ControllerMetrics;

import java.time.LocalDate;

public class IrrigationCalculator {

    public static double calculateEvapotranspiration(ControllerMetrics controllerMetrics, GrowthPhase growthPhase, boolean isRadiationMeasurement) {

        double cropCoefficient = growthPhase.getCropCoefficient();

        EvapotranspirationCalculator calculator = new EvapotranspirationCalculator(controllerMetrics);

        double etC = calculator.calculateETc(LocalDate.now().getDayOfYear(), cropCoefficient, isRadiationMeasurement);

        return etC;
    }

    /**
     * Calculates irrigation time for one plant.
     *
     * @param etc       evapotranspiration [mm/day]
     * @param distanceX distance between plants [cm]
     * @param distanceY distance between rows [cm]
     * @return irrigation time [minutes]
     */
    public static double calculateIrrigationDuration(double etc, int distanceX, int distanceY, double emitterFlow) {
        double areaPerPlant = (distanceX / 100.0) * (distanceY / 100.0);

        double litersNeeded = etc * areaPerPlant;

        return (litersNeeded / emitterFlow) * 60.0;
    }
}
