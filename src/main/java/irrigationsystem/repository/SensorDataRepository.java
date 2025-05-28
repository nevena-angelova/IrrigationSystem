package irrigationsystem.repository;

import irrigationsystem.dto.MeasureValuesDto;
import irrigationsystem.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    @Query(value = "SELECT * FROM get_latest_measure_values(:userId)", nativeQuery = true)
    List<MeasureValuesDto> getLatestValuesByUserId(@Param("userId") Long userId);
}
