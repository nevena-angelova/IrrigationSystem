package irrigationsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import irrigationsystem.dto.EtcStatisticDto;
import irrigationsystem.dto.ResponseDto;
import irrigationsystem.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/etc")
    @Operation(summary = "Get evapotranspiration", description = "Get evapotranspiration.")
    public ResponseEntity getEtcStatistics() {
        try {
            ResponseDto<List<EtcStatisticDto>> result = statisticService.getEtcStatistics();

            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }

            return ResponseEntity.ok(result.getValue());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }


}
