package irrigationsystem.repository;

import irrigationsystem.model.Device;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @EntityGraph(attributePaths = "sensors")
    Device getDeviceByUserId(Long userId);
}
