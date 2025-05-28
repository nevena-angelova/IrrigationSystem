package irrigationsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import irrigationsystem.dto.UserDto;
import irrigationsystem.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user by saving their information in the database.")
    public ResponseEntity register(@RequestBody UserDto userDto) {
        try{
            var result = authenticationService.register(userDto);
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Login as user, existing in the database.")
    public ResponseEntity login(@RequestBody UserDto userDto) {
        try{
            var result = authenticationService.login(userDto);
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
