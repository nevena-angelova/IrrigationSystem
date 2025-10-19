package irrigationsystem.controller;

import irrigationsystem.service.RelayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relay")
@RequiredArgsConstructor
public class RelayController {

    private final RelayService relayService;

    @PostMapping("/{deviceId}/on")
    public ResponseEntity turnOn(@PathVariable long deviceId) {
        try {
            var result = relayService.turnRelayOn(deviceId);

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/{deviceId}/off")
    public ResponseEntity turnOff(@PathVariable long deviceId) {
        try {
            var result = relayService.turnRelayOff(deviceId);

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
