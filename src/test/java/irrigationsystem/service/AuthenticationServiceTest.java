package irrigationsystem.service;

import irrigationsystem.dto.UserDto;
import irrigationsystem.entity.*;
import irrigationsystem.jwt.JwtUtil;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock ControllerRepository controllerRepository;
    @Mock SensorTypeRepository sensorTypeRepository;
    @Mock SensorRepository sensorRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;
    @Mock AuthenticationManager authenticationManager;
    @Mock Mapper mapper;
    @Mock Authentication authentication;
    @Mock UserDto userDto;
    @InjectMocks AuthenticationService service;

    @Test
    void register_shouldCreateUserControllerSensorsAndToken() {
        Role role = Role.builder().name("USER").build();
        when(roleRepository.findByName("USER")).thenReturn(role);
        when(userDto.getUsername()).thenReturn("anna");
        when(userDto.getPassword()).thenReturn("secret");
        when(userDto.getEmail()).thenReturn("anna@test.com");
        when(userDto.getFirstName()).thenReturn("Anna");
        when(userDto.getLastName()).thenReturn("Test");
        when(userDto.getControllerNumber()).thenReturn(12);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(controllerRepository.save(any(Controller.class))).thenAnswer(inv -> inv.getArgument(0));

        SensorType dht = new SensorType();
        dht.setName("DHT22");
        SensorType light = new SensorType();
        light.setName("BH1750");
        when(sensorTypeRepository.findByNameIn(anyList())).thenReturn(List.of(dht, light));
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token");

        var result = service.register(userDto);

        assertFalse(result.hasErrors());
        assertEquals("jwt-token", result.getValue().token());
        verify(userRepository).save(argThat(u -> "anna".equals(u.getUsername()) && "encoded".equals(u.getPassword())));
        verify(controllerRepository).save(argThat(c -> true));

        ArgumentCaptor<Iterable<Sensor>> captor = ArgumentCaptor.forClass(Iterable.class);
        verify(sensorRepository).saveAll(captor.capture());
        assertEquals(2, ((Collection<Sensor>) captor.getValue()).size());
    }

    @Test
    void register_shouldFailWhenUserRoleDoesNotExist() {
        when(roleRepository.findByName("USER")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.register(userDto));
        verifyNoInteractions(userRepository, controllerRepository, sensorRepository);
    }

    @Test
    void login_shouldReturnTokenForValidCredentials() {
        when(userDto.getUsername()).thenReturn("anna");
        when(userDto.getPassword()).thenReturn("secret");
        User user = new User();
        user.setUsername("anna");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

        var result = service.login(userDto);

        assertFalse(result.hasErrors());
        assertEquals("jwt-token", result.getValue().token());
    }

    @Test
    void login_shouldReturnErrorForInvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        var result = service.login(userDto);

        assertTrue(result.hasErrors());
        assertEquals("Invalid credentials", result.getErrorMessage().orElseThrow());
    }
}
