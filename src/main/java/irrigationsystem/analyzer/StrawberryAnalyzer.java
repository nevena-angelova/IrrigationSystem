package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class StrawberryAnalyzer extends Analyzer {
    public StrawberryAnalyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(plantId, growthPhase, measureValues);
    }

    @Override
    public ReportDto analyze() {

        if (getSoilMoisture() < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (getSoilMoisture() > getMaxSoilMoisture()) {
            addReportWarning("warning.strawberry.soil.too.wet");
        }

        if (getSoilMoisture() < 40) {
            addReportWarning("warning.strawberry.soil.critical.low");
        }

        if (getTemperature() < 5.0) {
            addReportWarning("warning.strawberry.temp.low");
        } else if (getTemperature() > 30.0) {
            addReportWarning("warning.strawberry.temp.high");
        }

        if (getHumidity() > 85 && getTemperature() >= 15 && getTemperature() <= 25) {
            addReportWarning("warning.strawberry.botrytis");
        }

        if (getHumidity() > 80 && getTemperature() > 25) {
            addReportWarning("warning.strawberry.powdery.mildew");
        }

        if (getHumidity() < 40 && getTemperature() > 28) {
            addReportWarning("warning.strawberry.hot.dry");
        }

        if (getTemperature() < 12 && getHumidity() > 90 && getSoilMoisture() > 80) {
            addReportWarning("warning.strawberry.root.rot");
        }

        if (getSoilMoisture() > 80 && getHumidity() > 90) {
            addReportWarning("warning.strawberry.fruit.mold");
        }

        if (getSoilMoisture() < 50 && getHumidity() < 40 && getTemperature() > 30) {
            addReportWarning("warning.strawberry.heat.stress");
        }

        if (getLight() < 20000) {
            addReportWarning("warning.strawberry.low.light");
        } else if (getLight() > 80000) {
            addReportWarning("warning.strawberry.high.light");
        }

        return getReport();
    }
}
