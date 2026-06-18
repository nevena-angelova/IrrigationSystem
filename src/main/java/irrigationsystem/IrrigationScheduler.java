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
   // @Scheduled(cron = "0 0 5 * * *", zone = "UTC")
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void checkAndWaterPlants() {
        irrigationService.processDailyIrrigation();
    }
}
