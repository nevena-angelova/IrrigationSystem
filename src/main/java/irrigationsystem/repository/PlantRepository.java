package irrigationsystem.repository;

import irrigationsystem.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    @Query(value = """
        SELECT COUNT(DISTINCT ps.plant_id) AS total_plants
        FROM plant_sensors ps
        JOIN sensors s ON ps.sensor_id = s.id
        WHERE s.controller_id = :controllerId;
        """, nativeQuery = true)
    int getPlantCount(@Param("controllerId") Integer controllerId);
}
