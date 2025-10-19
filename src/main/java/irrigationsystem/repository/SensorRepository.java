package irrigationsystem.repository;

import irrigationsystem.model.Sensor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    @EntityGraph(attributePaths = {"plant", "sensorType"})
    List<Sensor> findByDeviceId(Long deviceId);
}