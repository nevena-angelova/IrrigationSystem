package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class StrawberryAnalyzer  extends Analyzer {
    public StrawberryAnalyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(PlantId, growthPhase, measureValues);
    }

    @Override
    public ReportDto analyze() {

        double temperature = getTemperature();
        double maxSoilMoisture = getSoilMoisture();
        double soilMoisture = getSoilMoisture();

        if (maxSoilMoisture < getMinSoilMoisture()) {
            setReportNeedsIrrigation(true);
        }

        if (maxSoilMoisture > getMaxSoilMoisture()) {
            addReportWarning("Почвата е прекалено мокра – риск от гниене на корените.");
        }

        if (maxSoilMoisture < 40) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (temperature < 5.0) {
            addReportWarning("Температурата е твърде ниска за ягоди – риск от застой в развитието.");
        } else if (temperature > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (soilMoisture > 85 && temperature >= 15 && temperature <= 25) {
            addReportWarning("Висока влажност и умерена температура – риск от сиво гниене (Botrytis cinerea).");
        }

        if (soilMoisture > 80 && temperature > 25) {
            addReportWarning("Топло и влажно време – възможна поява на брашнеста мана по листата.");
        }

        if (soilMoisture < 40 && temperature > 28) {
            addReportWarning("Сух и горещ въздух – риск от загуба на влага, пригори и по-слабо плододаване.");
        }

        if (temperature < 12 && soilMoisture > 90 && maxSoilMoisture > 80) {
            addReportWarning("Хладно и влажно време – възможно развитие на кореново гниене (Phytophthora fragariae).");
        }

        if (maxSoilMoisture > 80 && soilMoisture > 90) {
            addReportWarning("Продължителна влажност – възможна поява на сиво гниене и плесен върху плодовете.");
        }

        if (maxSoilMoisture < 50 && soilMoisture < 40 && temperature > 30) {
            addReportWarning("Горещо и сухо време – растенията изпитват стрес, възможно спиране на плододаването.");
        }

        return getReport();
    }
}
