package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class PotatoAnalyzer extends Analyzer {
    public PotatoAnalyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(plantId, growthPhase, measureValues);
    }

    @Override
    public ReportDto analyze() {

        if (getSoilMoisture() < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (getSoilMoisture() > getMaxSoilMoisture()) {
            addReportWarning("warning.potato.soil.too.wet");
        }

        if (getSoilMoisture() < 35) {
            addReportWarning("warning.potato.soil.critical.low");
        }

        if (getTemperature() < 7.0) {
            addReportWarning("warning.potato.temp.low");
        } else if (getTemperature() > 30.0) {
            addReportWarning("warning.potato.temp.high");
        }

        if (getTemperature() >= 15 && getTemperature() <= 25 &&
            getHumidity() > 85 && getSoilMoisture() > 75) {

            addReportWarning("warning.potato.mildew");
        }

        if (getTemperature() > 25 && getHumidity() > 80) {
            addReportWarning("warning.potato.alternaria");
        }

        if (getTemperature() >= 10 && getTemperature() <= 20 &&
            getSoilMoisture() > 85) {

            addReportWarning("warning.potato.gray.mold");
        }

        if (getHumidity() < 40 && getTemperature() > 28 && getSoilMoisture() < 50) {
            addReportWarning("warning.potato.heat.dry");
        }

        if (getHumidity() > 90 && getTemperature() < 15) {
            addReportWarning("warning.potato.bacterial.rot");
        }

        if (getLight() < 15000) {
            addReportWarning("warning.potato.low.light");
        } else if (getLight() > 90000) {
            addReportWarning("warning.potato.high.light");
        }

        return getReport();
    }
}
