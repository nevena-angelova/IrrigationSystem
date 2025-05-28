package irrigationsystem.repository;

import irrigationsystem.model.MeasureType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasureTypeRepository extends JpaRepository<MeasureType, Long> {
    MeasureType findByName(String name);
    List<MeasureType> findByNameIn(List<String> names);
}
