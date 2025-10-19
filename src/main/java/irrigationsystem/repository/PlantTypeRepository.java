package irrigationsystem.repository;

import irrigationsystem.model.PlantType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantTypeRepository extends JpaRepository<PlantType, Long> {
}
