package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.time.LocalDate;
import java.util.Map;

public class CarrotAnalyzer extends Analyzer {
    public CarrotAnalyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
        super(PlantId, growthPhase, measureValues);
    }

    @Override
    public ReportDto analyze() {

        double temperature = getTemperature();
        double humidity = getHumidity();
        double pressure = getPressure().orElse(0.0);

        if (humidity < getMinHumidity()) {
            setReportNeedsIrrigation(true);
        }

        if (humidity > getMaxHumidity()) {
            addReportWarning("Почвата е прекалено мокра – риск от кореново гниене.");
        }

        if (humidity < 35) {
            addReportWarning("Критично ниска влажност – възможна деформация на корените");
        }

        if (temperature < 5.0) {
            addReportWarning("Температурата е твърде ниска – риск от застой в развитието.");
        } else if (temperature > 28.0) {
            addReportWarning("Прекалено висока температура – риск от преждевременно цъфтеж или горчив вкус");
        }

        if (temperature >= 18 && temperature <= 26 &&
                humidity > 75 && pressure < 1000) {
            addReportWarning("Висок риск от Alternaria (листно петно) – проверете листата.");
        }

        if (pressure < 995) {
            addReportWarning("Ниско атмосферно налягане – възможно застудяване или дъжд, висока влажност.");
        }

        return getReport();
    }
}
