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
            addReportWarning("Почвата е прекалено мокра – риск от кореново гниене.");
        }

        if (getSoilMoisture() < 35) {
            addReportWarning("Критично ниска влажност – възможна деформация на корените");
        }

        if (getTemperature() < 5.0) {
            addReportWarning("Температурата е твърде ниска – риск от застой в развитието.");
        } else if (getTemperature() > 28.0) {
            addReportWarning("Прекалено висока температура – риск от преждевременно цъфтеж или горчив вкус");
        }

        if (getHumidity() < 40.0) {
            addReportWarning("Влажността на въздуха е твърде ниска – ускорено изпарение и стрес за растенията.");
        } else if (getHumidity() > 85.0) {
            addReportWarning("Влажността на въздуха е прекалено висока – риск от брашнеста мана и бактериални инфекции.");
        }

        if (getHumidity() > 80.0 && getSoilMoisture() > 80.0) {
            addReportWarning("Висока влажност на въздуха и почвата – риск от плесен и гниене на корените.");
        }

        if (getTemperature() > 25 && getHumidity() > 80) {
            addReportWarning("Висока температура и влажност – риск от алтернария (черно листно петно).");
        }

        if (getTemperature() >= 15 && getTemperature() <= 25 && getHumidity() > 85 && getSoilMoisture() > 80) {
            addReportWarning("Прекалено влажна среда – възможна бяла плесен (Sclerotinia).");
        }

        if (getTemperature() > 20 && getSoilMoisture() > 85) {
            addReportWarning("Топла и влажна почва – риск от кореново гниене.");
        }

        if (getLight() < 15000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (getLight() > 90000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
