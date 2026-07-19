package irrigationsystem.repository;

import irrigationsystem.entity.EtcStatistic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtcStatisticRepository extends JpaRepository<EtcStatistic, Long> {
    @EntityGraph(attributePaths = "plant")
    List<EtcStatistic> findAll();
}
