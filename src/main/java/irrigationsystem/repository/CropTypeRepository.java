package irrigationsystem.repository;

import irrigationsystem.model.CropType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropTypeRepository extends JpaRepository<CropType, Long> {
}
