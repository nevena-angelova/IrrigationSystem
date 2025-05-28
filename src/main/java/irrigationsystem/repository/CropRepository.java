package irrigationsystem.repository;

import irrigationsystem.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> getCropsByUserId(Long userId);
}
