package irrigationsystem.analyzer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;

import java.time.LocalDate;
import java.util.Map;

public class StrawberryAnalyzer  extends Analyzer {
    public StrawberryAnalyzer(Long PlantId, GrowthPhase growthPhase, Map<MeasureTypeEnum, Double> measureValues) {
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
            addReportWarning("Почвата е прекалено мокра – риск от гниене на корените.");
        }

        if (humidity < 35) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (temperature < 5.0) {
            addReportWarning("Температурата е твърде ниска за ягоди – риск от застой в развитието.");
        } else if (temperature > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (temperature >= 15 && temperature <= 25 &&
                humidity > 75 && pressure < 1000) {
            addReportWarning("Висок риск от сиво гниене (Botrytis) – провери цветовете и плодовете.");
        }

        if (pressure < 995) {
            addReportWarning("Ниско атмосферно налягане – възможно застудяване или дъжд, висока влажност.");
        }

        return getReport();
    }
}
