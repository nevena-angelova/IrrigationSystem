package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class TomatoAnalyzer extends Analyzer {

    public TomatoAnalyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(plantId, growthPhase, measureValues);
    }

    @Override
    public ReportDto analyze() {

        if (getSoilMoisture() < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (getSoilMoisture() > getMaxSoilMoisture()) {
            addReportWarning("warning.tomato.soil.too.wet");
        }

        if (getSoilMoisture() < 40) {
            addReportWarning("warning.tomato.soil.critical.low");
        }

        if (getTemperature() < 12.0) {
            addReportWarning("warning.tomato.temp.low");
        } else if (getTemperature() > 32.0) {
            addReportWarning("warning.tomato.temp.high");
        }

        if (getHumidity() > 85 && getTemperature() >= 18 && getTemperature() <= 25) {
            addReportWarning("warning.tomato.mildew");
        }

        if (getHumidity() > 80 && getTemperature() > 26) {
            addReportWarning("warning.tomato.fungal.infections");
        }

        if (getHumidity() < 40 && getTemperature() > 30) {
            addReportWarning("warning.tomato.heat.dry.stress");
        }

        if (getSoilMoisture() > 80 && getHumidity() > 85) {
            addReportWarning("warning.tomato.moisture.excess");
        }

        if (getSoilMoisture() < 50 && getHumidity() < 40 && getTemperature() > 30) {
            addReportWarning("warning.tomato.fruit.deformation");
        }

        if (getTemperature() > 35 && getHumidity() < 35) {
            addReportWarning("warning.tomato.heat.shock");
        }

        if (getTemperature() < 12 && getHumidity() > 80 && getSoilMoisture() > 75) {
            addReportWarning("warning.tomato.bacterial.diseases");
        }

        if (getLight() < 20000) {
            addReportWarning("warning.tomato.low.light");
        } else if (getLight() > 100000) {
            addReportWarning("warning.tomato.high.light");
        }

        return getReport();
    }
}
