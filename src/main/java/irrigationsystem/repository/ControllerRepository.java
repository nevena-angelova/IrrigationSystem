package irrigationsystem.repository;

import irrigationsystem.entity.Controller;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControllerRepository extends JpaRepository<Controller, Long> {
    @EntityGraph(attributePaths = "sensors")
    Controller getControllerByUserId(Long userId);
}
