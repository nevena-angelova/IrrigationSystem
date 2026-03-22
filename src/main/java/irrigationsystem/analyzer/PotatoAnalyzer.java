package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class PotatoAnalyzer extends Analyzer {
    public PotatoAnalyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(PlantId, growthPhase, measureValues);
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
            addReportWarning("Почвата е прекалено мокра – риск от гниене на клубените.");
        }

        if (soilMoisture < 35) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (temperature < 7.0) {
            addReportWarning("Температурата е твърде ниска – риск от застой в развитието.");
        } else if (temperature > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (temperature >= 15 && temperature <= 25 && humidity > 85 && soilMoisture > 75) {
            addReportWarning("Висока влажност и умерена температура – риск от мана по картофите (Phytophthora infestans).");
        }

        if (temperature > 25 && humidity > 80) {
            addReportWarning("Топло и влажно време – възможна поява на алтернария (кафяви петна по листата).");
        }

        if (temperature >= 10 && temperature <= 20 && soilMoisture > 85) {
            addReportWarning("Почвата е прекалено влажна и хладна – възможно развитие на сиво гниене (Botrytis).");
        }

        if (humidity < 40 && temperature > 28 && soilMoisture < 50) {
            addReportWarning("Горещо и сухо време – риск от воден стрес и деформирани клубени.");
        }

        if (humidity > 90 && temperature < 15) {
            addReportWarning("Продължителна висока влажност и ниска температура – възможно развитие на бактериални гниения.");
        }

        if (light < 15000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (light > 90000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
