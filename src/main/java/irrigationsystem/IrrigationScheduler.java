package irrigationsystem;

import org.springframework.scheduling.annotation.Scheduled;

public class IrrigationScheduler {

/*    private final SensorDataService sensorDataService;
    private final MqttService mqttService;

    public IrrigationScheduler(SensorDataService sensorDataService,
                               MqttService mqttService) {
        this.sensorDataService = sensorDataService;
        this.mqttService = mqttService;
    }

    // Every day at 08:00
    @Scheduled(cron = "0 0 8 * * *")
    public void checkAndWaterPlants() {

        // Взимаш последните данни от БД
        SensorData latest = sensorDataService.getLatestData();

        if (latest == null) return;

        double soilMoisture = latest.getSoilMoisture();
        double temperature = latest.getTemperature();
        double light = latest.getLight();

        // Примерни условия
        if (soilMoisture < 30 && temperature > 20) {
            // Пускаш помпата през MQTT
            mqttService.publish("pump/control", "ON");
        }
    }*/
}
