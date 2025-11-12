package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.util.Map;

public class TomatoAnalyzer extends Analyzer {

    public TomatoAnalyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
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

        if (temperature < 12.0) {
            addReportWarning("Температурата е твърде ниска за домати. ");
        } else if (temperature > 32.0) {
            addReportWarning("Температурата е твърде висока – риск от стрес.");
        }

        if (soilMoisture > 85 && temperature >= 18 && temperature <= 25) {
            addReportWarning("Висока влажност и умерена температура – идеални условия за развитие на мана (Phytophthora infestans).");
        }

        if (soilMoisture > 80 && temperature > 26) {
            addReportWarning("Топло и влажно време – риск от брашнеста мана и гъбични инфекции по листата.");
        }

        if (soilMoisture < 40 && temperature > 30) {
            addReportWarning("Сух и горещ въздух – възможен стрес и върхово гниене (дефицит на калций).");
        }

        if (maxSoilMoisture > 80 && soilMoisture > 85) {
            addReportWarning("Продължителна влажност на почвата и въздуха – риск от мана и бактериално петносване.");
        }

        if (maxSoilMoisture < 50 && soilMoisture < 40 && temperature > 30) {
            addReportWarning("Горещо и сухо време – риск от пригори и деформации на плодовете.");
        }

        if (temperature > 35 && soilMoisture < 35) {
            addReportWarning("Изключително горещо и сухо – възможен топлинен шок, спиране на растежа и лошо опрашване.");
        }

        if (temperature < 12 && soilMoisture > 80 && maxSoilMoisture > 75) {
            addReportWarning("Хладно и влажно време – риск от бактериални болести по стъблата и листата.");
        }

        return getReport();
    }
}
