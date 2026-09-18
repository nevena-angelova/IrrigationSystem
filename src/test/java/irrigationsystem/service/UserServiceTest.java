package irrigationsystem.service;

import irrigationsystem.entity.User;
import irrigationsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    @Test
    void loadUserByUsername_shouldReturnUser() {
        User user = new User();
        user.setUsername("anna");
        when(userRepository.findByUsername("anna")).thenReturn(Optional.of(user));

        assertSame(user, userService.loadUserByUsername("anna"));
    }

    @Test
    void loadUserByUsername_shouldThrowWhenMissing() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("missing"));
    }
}
