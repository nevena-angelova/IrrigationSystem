package irrigationsystem.repository;

import irrigationsystem.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorTypeRepository extends JpaRepository<SensorType, Long> {
    List<SensorType> findByNameIn(List<String> names);
}
