package irrigationsystem.analyzer;

import java.time.LocalDate;

public class EvapotranspirationCalculator {

    private static final double ALPHA = 0.23;
    private static final double G_SC = 0.0820; /* [MJ/m/min] */
    private static final double SIGMA = 4.903e-9;
    private static final double K_RS = 0.16;
    private static final double GAMMA = 0.063;
    private static final double U2 = 2.0;

    /**
     * Calculate evapotranspiration ETc [mm/day]
     * @param tMin temperature at 24h [°C]
     * @param tMax temperature at 24h [°C]
     * @param tMean temperature at 24h [°C]
     * @param rhMin 24h min humidity [0-100%]
     * @param rhMax 24h max humidity [0-100%]
     * @param latitude latitude in degrees
     * @param altitude altitude [m]
     * @param dayOfYear day of the year
     * @param kc Kc factor for the crop
     * @return
     */
    public static double calculateETc(
        double tMin, double tMax, double tMean,
        double rhMin, double rhMax,
        double latitude,
        double altitude,
        int dayOfYear,
        double kc
    ) {

        double et0 = calculateET0(
            tMin, tMax, tMean,
            rhMin, rhMax,
            latitude,
            altitude,
            dayOfYear
        );

        return kc * et0;
    }

    /**
     * Reference evapotranspiration from Penman-Monteith equation ET0 [mm/day]
     * @param tMin 24-hour min temperature [°C]
     * @param tMax 24-hour max temperature [°C]
     * @param tMean 24-hour mean temperature [°C]
     * @param rhMin 24-hour min humidity [0-100%]
     * @param rhMax 24-hour max humidity [0-100%]
     * @param latitude latitude in degrees
     * @param altitude altitude [m]
     * @param j day of the year
     * @return ET0
     */
    public static double calculateET0(
        double tMin, double tMax, double tMean,
        double rhMin, double rhMax,
        double latitude,
        double altitude,
        int j
    ) {

        double es = saturationVaporPressure(tMax, tMin);
        double ea = actualVaporPressure(tMax, tMin, rhMax, rhMin);

        double delta = slopeVaporPressureCurve(tMean);

        double ra = extraterrestrialRadiation(latitude, j);
        double rs = solarRadiation(tMax, tMin, ra);
        double rso = clearSkyRadiation(altitude, ra);
        double rnl = netLongwaveRadiation(tMax, tMin, ea, rs, rso);
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
    private static double saturationVaporPressureAtTemperature(double temperature) {
        return 0.6108 * Math.exp((17.27 * temperature) / (temperature + 237.3));
    }

    /**
     * Saturation vapour pressure es [kPa]
     *
     * @param tMax 24-hour max temperature [°C]
     * @param tMin 24-hour min temperature [°C]
     * @return Saturation vapor pressure es [kPa]
     */
    private static double saturationVaporPressure(double tMax, double tMin) {
        return (saturationVaporPressureAtTemperature(tMax) + saturationVaporPressureAtTemperature(tMin)) / 2.0;
    }

    /**
     * Actual vapor pressure ea [kPa]
     *
     * @param tMax  24-hour max temperature [°C]
     * @param tMin  24-hour min temperature [°C]
     * @param rhMax 24-hour max humidity [0-100%]
     * @param rhMin 24-hour min humidity [0-100%]
     * @return
     */
    private static double actualVaporPressure(double tMax, double tMin, double rhMax, double rhMin) {
        double eTmin = saturationVaporPressureAtTemperature(tMin);
        double eTmax = saturationVaporPressureAtTemperature(tMax);

        return (eTmin * rhMax + eTmax * rhMin) / 200.0;
    }

    /**
     * Delta (slope) of the vapor pressure curve
     *
     * @param tMean 24-hour mean temperature
     * @return Delta
     */
    private static double slopeVaporPressureCurve(double tMean) {
        double e0 = saturationVaporPressureAtTemperature(tMean);

        return (4098.0 * e0) / Math.pow(tMean + 237.3, 2);
    }

    /**
     * Solar radiation Rs [MJ/m²/day⁻¹] (Hargreaves-Samani)
     * @param tMax 24-hour max temperature
     * @param tMin 24-hour min temperature
     * @param ra extraterrestrial radiation [MJ/m²/day]
     * @return Rs
     */
    private static double solarRadiation(double tMax, double tMin, double ra) {
        return K_RS * Math.sqrt(tMax - tMin) * ra;
    }

    /**
     * Clear sky radiation Rso [MJ/m²/day⁻¹]
     * @param altitude altitude [m]]
     * @param ra extraterrestrial radiation [MJ/m²/day]
     * @return Rso
     */
    private static double clearSkyRadiation(double altitude, double ra) {
        return (0.75 + 2e-5 * altitude) * ra;
    }

    /**
     * Net longwave radiation Rnl [MJ/m²/day]
     *
     * @param tMax 24-hour max temperature [°C]
     * @param tMin 24-hour min temperature [°C]
     * @param ea   actual vapor pressure [kPa]
     * @param rs   incoming solar radiation [MJ/m²/day]
     * @return Rnl
     */
    private static double netLongwaveRadiation(
        double tMax,
        double tMin,
        double ea,
        double rs,
        double rso) {

        double tKmax = tMax + 273.16;
        double tKmin = tMin + 273.16;

        double term1 = (Math.pow(tKmax, 4) + Math.pow(tKmin, 4)) / 2.0;
        double term2 = (0.34 - 0.14 * Math.sqrt(ea));

        double rsRso = (rso > 0) ? (rs / rso) : 0.0;

        double term3 = (1.35 * rsRso - 0.35);

        return SIGMA * term1 * term2 * term3;
    }

    /**
     *  Extraterrestrial radiation Ra [MJ/m²/day]
     * @param latitude latitude in degrees
     * @param j day of the year
     * @return Ra
     */
    private static double extraterrestrialRadiation(double latitude, int j) {

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
