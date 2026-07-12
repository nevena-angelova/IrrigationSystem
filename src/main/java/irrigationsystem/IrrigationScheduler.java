package irrigationsystem;

import irrigationsystem.service.IrrigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IrrigationScheduler {

    private final IrrigationService irrigationService;

    /**
     * The method is scheduled to run daily at 8:00 AM as defined by the cron expression.
     */
    //@Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 0 4 * * *", zone = "UTC")
    public void checkAndWaterPlants() {
        irrigationService.processDailyIrrigation();
    }
}
