package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class StrawberryAnalyzer  extends Analyzer {
    public StrawberryAnalyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues, boolean hasWater) {
        super(plantId, growthPhase, measureValues, hasWater);
    }

    @Override
    public ReportDto analyze() {

        double temperature = getTemperature();
        double soilMoisture = getSoilMoisture();
        double humidity = getHumidity();
        double light = getLight();

        if (soilMoisture < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (soilMoisture > getMaxSoilMoisture()) {
            addReportWarning("Почвата е прекалено мокра – риск от гниене на корените.");
        }

        if (soilMoisture < 40) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (temperature < 5.0) {
            addReportWarning("Температурата е твърде ниска за ягоди – риск от застой в развитието.");
        } else if (temperature > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (humidity > 85 && temperature >= 15 && temperature <= 25) {
            addReportWarning("Висока влажност и умерена температура – риск от сиво гниене (Botrytis cinerea).");
        }

        if (humidity > 80 && temperature > 25) {
            addReportWarning("Топло и влажно време – възможна поява на брашнеста мана по листата.");
        }

        if (humidity < 40 && temperature > 28) {
            addReportWarning("Сух и горещ въздух – риск от загуба на влага, пригори и по-слабо плододаване.");
        }

        if (temperature < 12 && humidity > 90 && soilMoisture > 80) {
            addReportWarning("Хладно и влажно време – възможно развитие на кореново гниене (Phytophthora fragariae).");
        }

        if (soilMoisture > 80 && humidity > 90) {
            addReportWarning("Продължителна влажност – възможна поява на сиво гниене и плесен върху плодовете.");
        }

        if (soilMoisture < 50 && humidity < 40 && temperature > 30) {
            addReportWarning("Горещо и сухо време – растенията изпитват стрес, възможно спиране на плододаването.");
        }

        if (light < 20000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (light > 80000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
