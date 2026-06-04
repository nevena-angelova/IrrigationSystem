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
            addReportWarning("Почвата е прекалено мокра – риск от гниене на клубените.");
        }

        if (getSoilMoisture() < 35) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (getTemperature() < 7.0) {
            addReportWarning("Температурата е твърде ниска – риск от застой в развитието.");
        } else if (getTemperature() > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (getTemperature() >= 15 && getTemperature() <= 25 && getHumidity() > 85 && getSoilMoisture() > 75) {
            addReportWarning("Висока влажност и умерена температура – риск от мана по картофите (Phytophthora infestans).");
        }

        if (getTemperature() > 25 && getHumidity() > 80) {
            addReportWarning("Топло и влажно време – възможна поява на алтернария (кафяви петна по листата).");
        }

        if (getTemperature() >= 10 && getTemperature() <= 20 && getSoilMoisture() > 85) {
            addReportWarning("Почвата е прекалено влажна и хладна – възможно развитие на сиво гниене (Botrytis).");
        }

        if (getHumidity() < 40 && getTemperature() > 28 && getSoilMoisture() < 50) {
            addReportWarning("Горещо и сухо време – риск от воден стрес и деформирани клубени.");
        }

        if (getHumidity() > 90 && getTemperature() < 15) {
            addReportWarning("Продължителна висока влажност и ниска температура – възможно развитие на бактериални гниения.");
        }

        if (getLight() < 15000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (getLight() > 90000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
