package irrigationsystem.repository;

import irrigationsystem.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    @Query(value = """
        SELECT COUNT(DISTINCT s.plant_id) AS total_plants
        FROM sensors s
        WHERE s.controller_id = :controllerId AND s.plant_id IS NOT NULL;
        """, nativeQuery = true)
    int getPlantCount(@Param("controllerId") Integer controllerId);
}
