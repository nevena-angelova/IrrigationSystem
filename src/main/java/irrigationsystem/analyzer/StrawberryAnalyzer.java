package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class StrawberryAnalyzer  extends Analyzer {
    public StrawberryAnalyzer(Long plantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(plantId, growthPhase, measureValues);
    }

    @Override
    public ReportDto analyze() {

        if (getSoilMoisture() < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (getSoilMoisture() > getMaxSoilMoisture()) {
            addReportWarning("Почвата е прекалено мокра – риск от гниене на корените.");
        }

        if (getSoilMoisture() < 40) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (getTemperature() < 5.0) {
            addReportWarning("Температурата е твърде ниска за ягоди – риск от застой в развитието.");
        } else if (getTemperature()  > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (getHumidity() > 85 && getTemperature()  >= 15 && getTemperature()  <= 25) {
            addReportWarning("Висока влажност и умерена температура – риск от сиво гниене (Botrytis cinerea).");
        }

        if (getHumidity()  > 80 && getTemperature()  > 25) {
            addReportWarning("Топло и влажно време – възможна поява на брашнеста мана по листата.");
        }

        if (getHumidity()  < 40 && getTemperature()  > 28) {
            addReportWarning("Сух и горещ въздух – риск от загуба на влага, пригори и по-слабо плододаване.");
        }

        if (getTemperature()  < 12 && getHumidity()  > 90 && getSoilMoisture() > 80) {
            addReportWarning("Хладно и влажно време – възможно развитие на кореново гниене (Phytophthora fragariae).");
        }

        if (getSoilMoisture() > 80 && getHumidity()  > 90) {
            addReportWarning("Продължителна влажност – възможна поява на сиво гниене и плесен върху плодовете.");
        }

        if (getSoilMoisture() < 50 && getHumidity()  < 40 && getTemperature()  > 30) {
            addReportWarning("Горещо и сухо време – растенията изпитват стрес, възможно спиране на плододаването.");
        }

        if (getLight() < 20000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (getLight() > 80000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
