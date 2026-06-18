package irrigationsystem.cache;


import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.PlantType;
import irrigationsystem.repository.GrowthPhaseRepository;
import irrigationsystem.repository.PlantTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final PlantTypeRepository plantTypeRepository;
    private final GrowthPhaseRepository growthPhaseRepository;

    @Cacheable("plantTypes")
    public List<PlantType> plantTypes() {
        return plantTypeRepository.findAll();
    }

    @Cacheable("growthPhases")
    public List<GrowthPhase> growthPhases() {
        return growthPhaseRepository.findAll();
    }

    public GrowthPhase getGrowthPhase(LocalDate plantingDate, Integer plantTypeId) {
        long days = ChronoUnit.DAYS.between(plantingDate, LocalDate.now());

        for (GrowthPhase phase : growthPhases()) {
            if (Objects.equals(phase.getPlantTypeId(), plantTypeId) &&  days >= phase.getStartDay() && days <= phase.getEndDay()) {
                return phase;
            }
        }

        return growthPhases().getLast();
    }

    public PlantType getPlantType(Integer plantTypeId) {
        for (PlantType plantType : plantTypes()) {
            if (Objects.equals(plantType.getId(), plantTypeId)) {
                return plantType;
            }
        }

        return null;
    }
}
