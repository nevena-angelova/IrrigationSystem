package irrigationsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import irrigationsystem.dto.CreateCropDto;
import irrigationsystem.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crop")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @GetMapping("/types")
    @Operation(summary = "Get all crop types", description = "Get all crop types from the database.")
    public ResponseEntity getCropTypes() {
        try {
            var result = cropService.getCropTypes();

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Create crop of selected type", description = "Create crop of selected type.")
    public ResponseEntity createCrop(@RequestBody CreateCropDto crop) {
        try {
            var result = cropService.createCrop(crop);

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/crops")
    @Operation(summary = "Get user crops", description = "Get all crops for particular user.")
    public ResponseEntity getCrops() {
        try {
            var result = cropService.getUserCrops();

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
