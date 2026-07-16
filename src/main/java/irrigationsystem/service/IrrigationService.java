package irrigationsystem.service;

import irrigationsystem.calculator.IrrigationCalculator;
import irrigationsystem.cache.CacheService;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.Plant;
import irrigationsystem.model.ControllerMetrics;
import irrigationsystem.model.PlantSoilMoistureData;
import irrigationsystem.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IrrigationService {
    private final SensorDataService sensorDataService;
    private final MqttService mqttService;
    private final PlantRepository plantRepository;
    private final CacheService cacheService;


    /**
     * Checks the soil moisture levels of all plants and performs irrigation if required.
     * <p>
     * It retrieves 24-hour controller metrics and the current soil moisture levels for plants
     * using the sensor data services. Each plant's growth phase and evapotranspiration rate
     * (ETc) are determined. If irrigation is needed, based on the soil moisture level, using the ETc,
     * the plant's x and y distance, and the emitter flow, the duration of the irrigation is calculated. Finally,
     * the appropriate relay is activated with the calculated duration via the MQTT service.
     * <p>
     * The updated evapotranspiration values are saved following the irrigation decisions.
     * <p>
     * Key operations performed:
     * - Retrieve 24-hour controller metrics and plant sensor data.
     * - Match plant data with its respective growth phase, soil moisture, and controller metrics.
     * - Calculate evapotranspiration and irrigation duration.
     * - Perform irrigation if the soil moisture level falls below the minimum threshold for the plant's growth phase.
     * - Accumulate evapotranspiration values when irrigation is not required.
     * - Save updated plant data to the repository.
     */

    public void processDailyIrrigation() {

        Map<Integer, ControllerMetrics> controllerMetrics = sensorDataService.getControllerMetrics(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));

        if (controllerMetrics.isEmpty()) {
            return;
        }

        List<PlantSoilMoistureData> sensorData = sensorDataService.getLatestPlantSensorData();

        if (sensorData.isEmpty()) {
            return;
        }

        List<Plant> plants = plantRepository.findAll();

        Map<Long, Plant> plantMap = plants.stream()
            .collect(Collectors.toMap(
                Plant::getId,
                Function.identity()
            ));

        for (PlantSoilMoistureData data : sensorData) {

            Plant plant = plantMap.get(data.getPlantId());

            if (plant == null) {
                continue;
            }

            ControllerMetrics metrics = controllerMetrics.get(data.getControllerId());

            if (metrics == null) {
                continue;
            }

            processPlant(data, plant, metrics);
        }

        plantRepository.saveAll(plants);
    }

    private void processPlant(PlantSoilMoistureData data, Plant plant, ControllerMetrics metrics) {

        GrowthPhase growthPhase = cacheService.getGrowthPhase(plant.getPlantingDate(), plant.getPlantTypeId());

        double dailyEtc = IrrigationCalculator.calculateEvapotranspiration(metrics, growthPhase, false);
        double dailyEtcMeasuredRadiation = IrrigationCalculator.calculateEvapotranspiration(metrics, growthPhase, true);

        log.info("Plant {} Soil moisture: {}, Metrics: tMin {}, tMax {}, tMean {}, rhMin {}, rhMax {}", plant.getId(), data.getSoilMoisture(), metrics.getMetrics().getTMin(), metrics.getMetrics().getTMax(), metrics.getMetrics().getTMean(), metrics.getMetrics().getRhMin(), metrics.getMetrics().getRhMax());
        log.info("Plant {} daily ETC: {}", plant.getId(), dailyEtc);
        log.info("Plant {} daily ETC using radiation measured by sensor: {}", plant.getId(), dailyEtcMeasuredRadiation);

        double accumulatedEtc = plant.getEtc() + dailyEtc;

        log.info("Plant {} accumulated ETC: {}", plant.getId(), accumulatedEtc);

        if (needsIrrigation(data, growthPhase)) {

            irrigatePlant(plant, data.getControllerId(), accumulatedEtc);

            plant.setEtc(0);
            return;
        }

        plant.setEtc(accumulatedEtc);
    }

    private boolean needsIrrigation(PlantSoilMoistureData data, GrowthPhase growthPhase) {
        return data.getSoilMoisture() < growthPhase.getMinSoilMoisture();
    }

    private void irrigatePlant(Plant plant, Integer controllerId, double etc) {

        double duration =
            IrrigationCalculator.calculateIrrigationDuration(
                etc,
                plant.getDistanceX(),
                plant.getDistanceY(),
                plant.getEmitterFlow()
            );

        log.info("Plant {} duration: {}", plant.getId(), duration);

        mqttService.irrigate(controllerId, plant.getAreaNumber(), duration);

        log.info(
            "Irrigating plant {}, controller {} for {} seconds",
            plant.getId(),
            controllerId,
            duration
        );
    }
}
