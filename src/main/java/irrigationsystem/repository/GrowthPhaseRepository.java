package irrigationsystem.repository;

import irrigationsystem.model.GrowthPhase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowthPhaseRepository extends JpaRepository<GrowthPhase, Integer> {
}
