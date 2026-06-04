package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class CarrotAnalyzer extends Analyzer {
    public CarrotAnalyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> values) {
        super(plantId, growthPhase, values);
    }

    @Override
    public ReportDto analyze() {

        if (getSoilMoisture() < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (getSoilMoisture() > getMaxSoilMoisture()) {
            addReportWarning("warning.carrot.soil.too.wet");
        }

        if (getSoilMoisture() < 35) {
            addReportWarning("warning.carrot.soil.critical.low");
        }

        if (getTemperature() < 5.0) {
            addReportWarning("warning.carrot.temp.low");
        } else if (getTemperature() > 28.0) {
            addReportWarning("warning.carrot.temp.high");
        }

        if (getHumidity() < 40.0) {
            addReportWarning("warning.carrot.humidity.low");
        } else if (getHumidity() > 85.0) {
            addReportWarning("warning.carrot.humidity.high");
        }

        if (getHumidity() > 80.0 && getSoilMoisture() > 80.0) {
            addReportWarning("warning.carrot.soil.air.high.moisture");
        }

        if (getTemperature() > 25 && getHumidity() > 80) {
            addReportWarning("warning.carrot.alternaria");
        }

        if (getTemperature() >= 15 && getTemperature() <= 25 &&
            getHumidity() > 85 && getSoilMoisture() > 80) {

            addReportWarning("warning.carrot.sclerotinia");
        }

        if (getTemperature() > 20 && getSoilMoisture() > 85) {
            addReportWarning("warning.carrot.root.rot");
        }

        if (getLight() < 15000) {
            addReportWarning("warning.carrot.low.light");
        } else if (getLight() > 90000) {
            addReportWarning("warning.carrot.high.light");
        }

        return getReport();
    }
}
