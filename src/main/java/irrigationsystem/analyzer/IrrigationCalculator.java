package irrigationsystem.analyzer;

import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.model.ControllerMetrics;

import java.time.LocalDate;

public class IrrigationCalculator {

    public static double calculateEvapotranspiration(ControllerMetrics controllerMetrics, GrowthPhase growthPhase) {

        double cropCoefficient = growthPhase.getCropCoefficient();

        double etC = EvapotranspirationCalculator.calculateETc(
            controllerMetrics.getMetrics().getTMin(),
            controllerMetrics.getMetrics().getTMax(),
            controllerMetrics.getMetrics().getTMean(),
            controllerMetrics.getMetrics().getRhMin(),
            controllerMetrics.getMetrics().getRhMax(),
            controllerMetrics.getLatitude(),
            controllerMetrics.getAltitude(),
            LocalDate.now().getDayOfYear(),
            cropCoefficient
        );

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
