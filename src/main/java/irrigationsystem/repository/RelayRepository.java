package irrigationsystem.repository;

import irrigationsystem.model.Relay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelayRepository extends JpaRepository<Relay, Long> {
}