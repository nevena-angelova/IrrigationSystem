package irrigationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import irrigationsystem.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
