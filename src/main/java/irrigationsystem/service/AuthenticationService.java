package irrigationsystem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import irrigationsystem.entity.*;
import irrigationsystem.dto.JwtResponse;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import irrigationsystem.jwt.JwtUtil;
import irrigationsystem.dto.ResponseDto;
import irrigationsystem.dto.UserDto;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ControllerRepository controllerRepository;
    private final SensorTypeRepository sensorTypeRepository;
    private final SensorRepository sensorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final Mapper mapper;

    @Transactional
    public ResponseDto<JwtResponse> register(UserDto userDto) {

        Role role = roleRepository.findByName(RoleEnum.USER.name());
        if (role == null) {
            throw new RuntimeException("Role USER not found");
        }

        User user = createUser(userDto);
        user.setRole(role);

        user = userRepository.save(user);

        Controller controller = new Controller();
        controller.setNumber(userDto.getControllerNumber());
        user.addController(controller);

        controller = controllerRepository.save(controller);

        List<SensorType> defaultSensorTypes =
            sensorTypeRepository.findByNameIn(List.of(
                SensorTypeEnum.DHT22.getValue(),
                SensorTypeEnum.BH1750.getValue()
            ));

        List<Sensor> sensors = new ArrayList<>();

        for (SensorType sensorType : defaultSensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(sensorType);
            sensor.setController(controller);
            sensors.add(sensor);
        }

        sensorRepository.saveAll(sensors);

        JwtResponse jwtResponse = new JwtResponse(jwtUtil.generateToken(user));

        return ResponseDto.<JwtResponse>builder()
            .value(jwtResponse)
            .build();
    }

    public ResponseDto<JwtResponse> login(UserDto userDto) {
        try {
          /*
          Check user in the database, it is configured to filter using UserService
           */
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

            var user = (User) authentication.getPrincipal();

            JwtResponse jwtResponse = new JwtResponse(jwtUtil.generateToken(user));

            return ResponseDto.<JwtResponse>builder().value(jwtResponse).build();

        } catch (AuthenticationException e) {

            return ResponseDto.<JwtResponse>builder().errorMessage(Optional.of("Invalid credentials")).hasErrors(true).build();
        }
    }

    private User createUser(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setUpdated(now);

        return user;
    }
}
