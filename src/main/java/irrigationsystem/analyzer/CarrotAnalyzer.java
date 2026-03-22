package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class CarrotAnalyzer extends Analyzer {
    public CarrotAnalyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> values) {
        super(PlantId, growthPhase, values);
    }

    @Override
    public ReportDto analyze() {

        double temperature = getTemperature();
        double humidity = getHumidity();
        double soilMoisture = getSoilMoisture();
        double light = getLight();

        if (soilMoisture < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (soilMoisture > getMaxSoilMoisture()) {
            addReportWarning("Почвата е прекалено мокра – риск от кореново гниене.");
        }

        if (soilMoisture < 35) {
            addReportWarning("Критично ниска влажност – възможна деформация на корените");
        }

        if (temperature < 5.0) {
            addReportWarning("Температурата е твърде ниска – риск от застой в развитието.");
        } else if (temperature > 28.0) {
            addReportWarning("Прекалено висока температура – риск от преждевременно цъфтеж или горчив вкус");
        }

        if (humidity < 40.0) {
            addReportWarning("Влажността на въздуха е твърде ниска – ускорено изпарение и стрес за растенията.");
        } else if (humidity > 85.0) {
            addReportWarning("Влажността на въздуха е прекалено висока – риск от брашнеста мана и бактериални инфекции.");
        }

        if (humidity > 80.0 && soilMoisture > 80.0) {
            addReportWarning("Висока влажност на въздуха и почвата – риск от плесен и гниене на корените.");
        }

        if (temperature > 25 && humidity > 80) {
            addReportWarning("Висока температура и влажност – риск от алтернария (черно листно петно).");
        }

        if (temperature >= 15 && temperature <= 25 && humidity > 85 && soilMoisture > 80) {
            addReportWarning("Прекалено влажна среда – възможна бяла плесен (Sclerotinia).");
        }

        if (temperature > 20 && soilMoisture > 85) {
            addReportWarning("Топла и влажна почва – риск от кореново гниене.");
        }

        if (light < 15000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (light > 90000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
