package irrigationsystem.mqtt;

import irrigationsystem.model.Plant;
import irrigationsystem.model.MeasureTypeEnum;
import irrigationsystem.model.SensorData;

import java.util.List;
import java.util.Map;

public record SensorProcessingResult(
        Map<Plant, Map<MeasureTypeEnum, Double>> plantMeasures,
        List<SensorData> sensorData
) {}
