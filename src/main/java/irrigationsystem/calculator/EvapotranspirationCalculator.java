package irrigationsystem.calculator;

import irrigationsystem.model.ControllerMetrics;
import irrigationsystem.model.LightData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class EvapotranspirationCalculator {

    private static final double ALPHA = 0.23;
    private static final double G_SC = 0.0820; /* [MJ/m/min] */
    private static final double SIGMA = 4.903e-9;
    private static final double K_RS = 0.16;
    private static final double GAMMA = 0.063;
    private static final double U2 = 2.0;
    private static final double LUX_TO_W_M2 = 0.0079;

    private static double tMin; // temperature at 24h [°C]
    private static double tMax; // temperature at 24h [°C]
    private static double tMean; // temperature at 24h [°C]
    private static double rhMin; // 24h min humidity [0-100%]
    private static double rhMax; // 24h max humidity [0-100%]
    private static double latitude; // latitude in degrees
    private static double altitude; // altitude [m]
    private static List<LightData> lightData;

    public EvapotranspirationCalculator(ControllerMetrics controllerMetrics) {
        tMin = controllerMetrics.getMetrics().getTMin();
        tMax = controllerMetrics.getMetrics().getTMax();
        tMean = controllerMetrics.getMetrics().getTMean();
        rhMin = controllerMetrics.getMetrics().getRhMin();
        rhMax = controllerMetrics.getMetrics().getRhMax();
        latitude = controllerMetrics.getLatitude();
        altitude = controllerMetrics.getAltitude();
        lightData = controllerMetrics.getLightData();
    }

    /**
     * Calculate evapotranspiration ETc [mm/day]
     *
     * @param dayOfYear day of the year
     * @param kc        Kc factor for the crop
     * @return
     */
    public double calculateETc(int dayOfYear, double kc, boolean isRadiationMeasurement) {

        double et0 = calculateET0(dayOfYear, isRadiationMeasurement);

        return kc * et0;
    }

    /**
     * Reference evapotranspiration from Penman-Monteith equation ET0 [mm/day]
     *
     * @param j day of the year
     * @return ET0
     */
    public double calculateET0(int j, boolean isRadiationMeasurement) {

        double es = saturationVaporPressure();
        double ea = actualVaporPressure();

        double delta = slopeVaporPressureCurve();

        double ra = extraterrestrialRadiation(j);
        double rs = isRadiationMeasurement ? solarRadiationUsingMeasurement() : solarRadiation(ra);
        double rso = clearSkyRadiation(ra);
        double rnl = netLongwaveRadiation(ea, rs, rso);
        double rn = (1 - ALPHA) * rs - rnl;

        double g = 0;

        double numerator =
            0.408 * delta * (rn - g)
                + GAMMA * (900 / (tMean + 273)) * U2 * (es - ea);

        double denominator =
            delta + GAMMA * (1 + 0.34 * U2);

        return numerator / denominator;
    }

    /**
     * Saturation vapor pressure e°(T) [kPa]
     *
     * @param temperature temperature [°C]
     * @return saturation vapor pressure
     */
    private double saturationVaporPressureAtTemperature(double temperature) {
        return 0.6108 * Math.exp((17.27 * temperature) / (temperature + 237.3));
    }

    /**
     * Saturation vapour pressure es [kPa]
     *
     * @return Saturation vapor pressure es [kPa]
     */
    private double saturationVaporPressure() {
        return (saturationVaporPressureAtTemperature(tMax) + saturationVaporPressureAtTemperature(tMin)) / 2.0;
    }

    /**
     * Actual vapor pressure ea [kPa]
     *
     * @return
     */
    private double actualVaporPressure() {
        double eTmin = saturationVaporPressureAtTemperature(tMin);
        double eTmax = saturationVaporPressureAtTemperature(tMax);

        return (eTmin * rhMax + eTmax * rhMin) / 200.0;
    }

    /**
     * Delta (slope) of the vapor pressure curve
     *
     * @return Delta
     */
    private double slopeVaporPressureCurve() {
        double e0 = saturationVaporPressureAtTemperature(tMean);

        return (4098.0 * e0) / Math.pow(tMean + 237.3, 2);
    }

    /**
     * Solar radiation Rs [MJ/m²/day⁻¹] (Hargreaves-Samani)
     *
     * @param ra extraterrestrial radiation [MJ/m²/day]
     * @return Rs
     */
    private static double solarRadiation(double ra) {

        double sr = K_RS * Math.sqrt(tMax - tMin) * ra;

        log.info("Solar radiation: {}", sr);

        return sr;
    }

    /**
     * Calculates incoming solar radiation (Rs) from BH1750 measurements.
     *
     * @return Rs [MJ/m²/day]
     */
    private static double solarRadiationUsingMeasurement() {

        if (lightData == null || lightData.size() < 2) {
            return 0.0;
        }

        List<LightData> sortedData = lightData.stream()
            .sorted(Comparator.comparing(LightData::getCreationDate))
            .toList();

        double energyJ = 0.0;

        for (int i = 0; i < sortedData.size() - 1; i++) {

            LightData current = sortedData.get(i);
            LightData next = sortedData.get(i + 1);

            long seconds = Duration.between(current.getCreationDate(), next.getCreationDate()).getSeconds();

            double lux1 = current.getValue();
            double lux2 = next.getValue();

            double irradiance1 = lux1 * LUX_TO_W_M2;
            double irradiance2 = lux2 * LUX_TO_W_M2;

            double averageIrradiance = (irradiance1 + irradiance2) / 2.0;

            energyJ += averageIrradiance * seconds;
        }

        double sr = energyJ / 1_000_000.0;

        log.info("Solar radiation using measurement: {}", sr);

        return sr;
    }

    /**
     * Clear sky radiation Rso [MJ/m²/day⁻¹]
     *
     * @param ra extraterrestrial radiation [MJ/m²/day]
     * @return Rso
     */
    private static double clearSkyRadiation(double ra) {
        return (0.75 + 2e-5 * altitude) * ra;
    }

    /**
     * Net longwave radiation Rnl [MJ/m²/day]
     *
     * @param ea actual vapor pressure [kPa]
     * @param rs incoming solar radiation [MJ/m²/day]
     * @return Rnl
     */
    private static double netLongwaveRadiation(double ea, double rs, double rso) {

        double tKmax = tMax + 273.16;
        double tKmin = tMin + 273.16;

        double term1 = (Math.pow(tKmax, 4) + Math.pow(tKmin, 4)) / 2.0;
        double term2 = (0.34 - 0.14 * Math.sqrt(ea));

        double rsRso = (rso > 0) ? (rs / rso) : 0.0;

        double term3 = (1.35 * rsRso - 0.35);

        return SIGMA * term1 * term2 * term3;
    }

    /**
     * Extraterrestrial radiation Ra [MJ/m²/day]
     *
     * @param j day of the year
     * @return Ra
     */
    private static double extraterrestrialRadiation(int j) {

        double latRad = Math.toRadians(latitude);

        double dr = 1 + 0.033 * Math.cos(2 * Math.PI * j / 365.0);
        double delta = 0.409 * Math.sin(2 * Math.PI * j / 365.0 - 1.39);

        double ws = Math.acos(-Math.tan(latRad) * Math.tan(delta));

        return (24 * 60 / Math.PI)
            * G_SC
            * dr
            * (ws * Math.sin(latRad) * Math.sin(delta) + Math.cos(latRad) * Math.cos(delta) * Math.sin(ws));
    }
}
