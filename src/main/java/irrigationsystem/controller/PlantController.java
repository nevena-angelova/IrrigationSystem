package irrigationsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import irrigationsystem.dto.CreatePlantDto;
import irrigationsystem.service.PlantService;
import irrigationsystem.service.RelayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/types")
    @Operation(summary = "Get all Plant types", description = "Get all Plant types from the database.")
    public ResponseEntity getPlantTypes() {
        try {
            var result = plantService.getPlantTypes();

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Create Plant of selected type", description = "Create Plant of selected type.")
    public ResponseEntity createPlant(@RequestBody CreatePlantDto Plant) {
        try {
            var result = plantService.createPlant(Plant);

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/plants")
    @Operation(summary = "Get user Plants", description = "Get all Plants for particular user.")
    public ResponseEntity getPlants() {
        try {
            var result = plantService.getUserPlants();

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }


    private final RelayService relayService;

    @PostMapping("/irrigate/{deviceId}/{relayId}/{irrigationDuration}")
    public ResponseEntity irrigate(@PathVariable Integer deviceId, @PathVariable Integer relayId, @PathVariable Integer irrigationDuration) {
        try {
            var result = plantService.irrigate(deviceId, relayId, irrigationDuration);

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
