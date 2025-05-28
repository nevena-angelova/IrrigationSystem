package irrigationsystem.service;

import java.time.LocalDateTime;
import java.util.Optional;

import irrigationsystem.dto.JwtResponse;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.model.RoleEnum;
import irrigationsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import irrigationsystem.jwt.JwtUtil;
import irrigationsystem.repository.UserRepository;
import irrigationsystem.model.User;
import irrigationsystem.model.Role;
import irrigationsystem.dto.ResponseDto;
import irrigationsystem.dto.UserDto;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final Mapper mapper;

    public ResponseDto<JwtResponse> register(UserDto userDto) {

        Role role = roleRepository.findByName(RoleEnum.USER.name());

        var user = mapper.fromUserDto(userDto);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (user.getId() == null) {
            user.setCreated(LocalDateTime.now());
        }

        user.setUpdated(LocalDateTime.now());

        userRepository.save(user);

        JwtResponse jwtResponse = new JwtResponse(jwtUtil.generateToken(user));

        return ResponseDto.<JwtResponse>builder().value(jwtResponse).build();
    }

    public ResponseDto<JwtResponse> login(UserDto userDto) {
        try {
            // check user in the database, it is configured to filter using UserService
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

            var user = (User) authentication.getPrincipal();

            JwtResponse jwtResponse = new JwtResponse(jwtUtil.generateToken(user));

            return ResponseDto.<JwtResponse>builder().value(jwtResponse).build();

        } catch (AuthenticationException e) {

            return ResponseDto.<JwtResponse>builder().errorMessage(Optional.of("Invalid credentials")).hasErrors(true).build();
        }
    }
}
