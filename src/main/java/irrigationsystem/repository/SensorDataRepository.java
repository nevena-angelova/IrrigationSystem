package irrigationsystem.repository;

import irrigationsystem.model.PlantSensorData;
import irrigationsystem.model.ControllerSensorData;
import irrigationsystem.entity.SensorData;
import irrigationsystem.model.PlantSoilMoistureData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    @Query(value = """
        SELECT DISTINCT ON (sd.sensor_id, sd.measure_type_id)
            p.id AS plant_id,
            p.plant_type_id,
            pt.name AS  plant_type_name,
            p.planting_date,
            sd.sensor_id,
            sd.measure_type_id,
            mt.name AS measure_type,
            sd.value,
            sd.creation_date,
            d.id AS device_id,
            p.area_number
        FROM plants p
        JOIN plant_sensors ps ON p.id = ps.plant_id
        JOIN sensors s ON s.id = ps.sensor_id
        JOIN controllers d ON d.id = s.controller_id
        JOIN sensor_data sd ON sd.sensor_id = s.id
        JOIN plant_types pt ON pt.id = p.plant_type_id
        JOIN measure_types mt ON mt.id = sd.measure_type_id
        WHERE d.user_id = :userId
        ORDER BY sd.sensor_id, sd.measure_type_id, sd.creation_date DESC;
        """, nativeQuery = true)
    List<PlantSensorData> getLatestValuesByUserId(@Param("userId") Long userId);

    @Query(value = """
        SELECT
            c.id AS controllerId,
            c.altitude AS altitude,
            c.latitude AS latitude,
            sd.sensor_id AS sensorId,
            sd.measure_type_id AS measureTypeId,
            sd.value AS value,
            sd.creation_date AS creationDate,
            mt.name AS measureType
        FROM controllers c
        JOIN sensors s ON c.id = s.controller_id
        JOIN sensor_data sd ON s.id = sd.sensor_id
        JOIN measure_types mt ON mt.id = sd.measure_type_id
        WHERE sd.creation_date >= :from
          AND sd.measure_type_id <> :soilMoistureTypeId
        ORDER BY sd.creation_date DESC
        """, nativeQuery = true)
    List<ControllerSensorData> getDataFrom(
        @Param("from") LocalDateTime from,
        @Param("soilMoistureTypeId") Integer soilMoistureTypeId
    );

    @Query(value = """
        SELECT DISTINCT ON (sd.sensor_id)
            s.controller_id,
            ps.plant_id,
            sd.value as soil_moisture
        FROM plant_sensors ps
        JOIN sensors  s ON s.id = ps.sensor_id
        JOIN sensor_data sd ON s.id = sd.sensor_id
        WHERE sd.measure_type_id = :soilMoistureTypeId
        ORDER BY sd.sensor_id, sd.creation_date DESC;
        """, nativeQuery = true)
    List<PlantSoilMoistureData> getPlantSensorData(
        @Param("soilMoistureTypeId") Integer soilMoistureTypeId
    );
}
