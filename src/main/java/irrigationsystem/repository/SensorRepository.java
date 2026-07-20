package irrigationsystem.repository;

import irrigationsystem.entity.Sensor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    @EntityGraph(attributePaths = {"plant", "sensorType"})
    List<Sensor> findByControllerIdOrderByIdAsc(Integer controllerId);
}
