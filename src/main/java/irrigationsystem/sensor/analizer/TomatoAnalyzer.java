package irrigationsystem.sensor.analizer;

import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.MeasureTypeEnum;
import irrigationsystem.sensor.lifecycle.Lifecycle;

import java.time.LocalDate;
import java.util.Map;

public class TomatoAnalyzer extends Analyzer {

    public TomatoAnalyzer(Long cropId, LocalDate plantingDate, Lifecycle lifecycle, Map<MeasureTypeEnum, Double> measureValues) {
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
            addReportWarning("Почвата е прекалено мокра – риск от гниене на корените.");
        }

        if (humidity < 35) {
            addReportWarning("Критично ниска влажност – възможно завяхване.");
        }

        if (temperature < 12.0) {
            addReportWarning("Температурата е твърде ниска за домати. ");
        } else if (temperature > 32.0) {
            addReportWarning("Температурата е твърде висока – риск от стрес.");
        }

        if (pressure < 995) {
            addReportWarning("Ниско атмосферно налягане – възможен дъжд, висока влажност.");
        }

        if (temperature >= 18 && temperature <= 26 &&
                humidity > 75 && pressure < 1000) {
            addReportWarning("Идеални условия за гъбични заболявания като мана – повишено внимание!");
        }

        return getReport();
    }
}
