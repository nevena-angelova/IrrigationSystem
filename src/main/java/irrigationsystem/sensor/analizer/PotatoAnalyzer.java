package irrigationsystem.sensor.analizer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.MeasureTypeEnum;
import irrigationsystem.sensor.lifecycle.Lifecycle;

import java.time.LocalDate;
import java.util.Map;

public class PotatoAnalyzer extends Analyzer {
    public PotatoAnalyzer(Long cropId, LocalDate plantingDate, Lifecycle lifecycle, Map<MeasureTypeEnum, Double> measureValues) {
        super(cropId, plantingDate, lifecycle, measureValues);
    }

    @Override
    public ReportDto analyze() {
        double temperature = getTemperature();
        double humidity = getHumidity();
        double pressure = getPressure();

        if (humidity < getMinHumidity()) {
            setReportNeedsIrrigation(true);
        }

        if (humidity > getMaxHumidity()) {
            addReportWarning("Почвата е прекалено мокра – риск от гниене на клубените.");
        }

        if (humidity < 35) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (temperature < 7.0) {
            addReportWarning("Температурата е твърде ниска – риск от застой в развитието.");
        } else if (temperature > 30.0) {
            addReportWarning("Прекалено висока температура – възможен топлинен и воден стрес.");
        }

        if (temperature >= 15 && temperature <= 25 &&
                humidity > 80 && pressure < 1000) {
            addReportWarning("Висок риск от мана (Phytophthora) – проверете листата и стъблата.");
        }

        if (pressure < 995) {
            addReportWarning("Ниско атмосферно налягане – възможно застудяване или дъжд, висока влажност.");
        }

        return getReport();
    }
}
