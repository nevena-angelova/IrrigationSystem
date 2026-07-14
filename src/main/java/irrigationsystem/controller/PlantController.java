package irrigationsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import irrigationsystem.dto.*;
import irrigationsystem.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/types")
    @Operation(summary = "Get all Plant types", description = "Get all Plant types from the database.")
    public ResponseEntity getPlantTypes() {
        try {
            ResponseDto<List<PlantTypeDto>> result = plantService.getPlantTypes();

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Create Plant of selected type", description = "Create Plant of selected type.")
    public ResponseEntity createPlant(@RequestBody CreatePlantDto Plant) {
        try {
            ResponseDto<String> result = plantService.createPlant(Plant);

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
            ResponseDto<List<PlantReportDto>> result = plantService.getUserPlantReports();

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
