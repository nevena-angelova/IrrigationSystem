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
            addReportWarning("Почвата е прекалено мокра – риск от гниене на корените.");
        }

        if (getSoilMoisture() < 40) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (getTemperature() < 12.0) {
            addReportWarning("Температурата е твърде ниска за домати. ");
        } else if (getTemperature()  > 32.0) {
            addReportWarning("Температурата е твърде висока – риск от стрес.");
        }

        if (getHumidity() > 85 && getTemperature()  >= 18 && getTemperature()  <= 25) {
            addReportWarning("Висока влажност и умерена температура – идеални условия за развитие на мана (Phytophthora infestans).");
        }

        if (getHumidity() > 80 && getTemperature()  > 26) {
            addReportWarning("Топло и влажно време – риск от брашнеста мана и гъбични инфекции по листата.");
        }

        if (getHumidity() < 40 && getTemperature() > 30) {
            addReportWarning("Сух и горещ въздух – възможен стрес и върхово гниене (дефицит на калций).");
        }

        if (getSoilMoisture() > 80 && getHumidity() > 85) {
            addReportWarning("Продължителна влажност на почвата и въздуха – риск от мана и бактериално петносване.");
        }

        if (getSoilMoisture() < 50 && getHumidity() < 40 && getTemperature() > 30) {
            addReportWarning("Горещо и сухо време – риск от пригори и деформации на плодовете.");
        }

        if (getTemperature() > 35 && getHumidity() < 35) {
            addReportWarning("Изключително горещо и сухо – възможен топлинен шок, спиране на растежа и лошо опрашване.");
        }

        if (getTemperature() < 12 && getHumidity() > 80 && getSoilMoisture() > 75) {
            addReportWarning("Хладно и влажно време – риск от бактериални болести по стъблата и листата.");
        }

        if (getLight() < 20000) {
            addReportWarning("Недостатъчно осветление – растенията може да забавят растежа и плододаването.");
        } else if (getLight() > 100000) {
            addReportWarning("Прекалено силно осветление – възможен стрес за растенията, риск от изгаряне на листата.");
        }

        return getReport();
    }
}
