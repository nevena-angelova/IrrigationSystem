package irrigationsystem.scheduler;

import irrigationsystem.controller.NotificationController;
import irrigationsystem.model.*;
import irrigationsystem.repository.CropRepository;
import irrigationsystem.repository.SensorDataRepository;
import irrigationsystem.sensor.analizer.Analyzer;
import irrigationsystem.sensor.analizer.AnalyzerFactory;
import irrigationsystem.dto.ReportDto;
import irrigationsystem.sensor.reader.ReaderFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SensorScheduler  {

    private final CropRepository cropRepository;
    private final SensorDataRepository sensorDataRepository;
    private final NotificationController notificationController;

    @Scheduled(fixedRate = 8000)
    @Transactional //This keeps the Hibernate session open during execution
    public void readSensor() {

        // Get crop data from database

        var crops = cropRepository.findAll();

        if (crops.isEmpty()) {
            return;
        }

        // Read crops sensor data

        var data = new ArrayList<SensorData>();

        for (var crop : crops) {

            var sensors = crop.getSensors();
            Map<MeasureTypeEnum, Double> measureValues = new HashMap<>();

            for (var sensor : sensors) {

                var measureTypes = sensor.getSensorType().getMeasureTypes();

                for (var type : measureTypes) {

                    var measureType = MeasureTypeEnum.valueOf(type.getName());
                    var reader = ReaderFactory.createSensorReader(measureType);
                    var value = reader.read();

                    data.add(createSensorData(sensor, value, type));

                    measureValues.put(measureType, value);
                }
            }

            // Analyze measured values

            var cropType = CropTypeEnum.getValue(crop.getCropType().getId());

            Analyzer analyzer = AnalyzerFactory.CreateAnalyzer(crop.getId(), crop.getPlantingDate(), cropType, measureValues);

            ReportDto report = analyzer.analyze();

            // TODO: Email notification

            notificationController.sendReport(report);
        }

        // Bulk save data in database

        sensorDataRepository.saveAll(data);
    }


    private SensorData createSensorData(Sensor sensor, double value, MeasureType measureType) {
        var data = new SensorData();
        data.setSensor(sensor);
        data.setValue(value);
        data.setTimestamp(OffsetDateTime.now());
        data.setMeasureType(measureType);
        return data;
    }
}
