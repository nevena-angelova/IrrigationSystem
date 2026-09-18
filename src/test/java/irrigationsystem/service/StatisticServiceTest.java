package irrigationsystem.service;

import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.EtcStatisticDto;
import irrigationsystem.entity.EtcStatistic;
import irrigationsystem.entity.Plant;
import irrigationsystem.entity.PlantType;
import irrigationsystem.repository.EtcStatisticRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {
    @Mock EtcStatisticRepository repository;
    @Mock CacheService cacheService;
    @InjectMocks StatisticService service;

    @Test
    void getEtcStatistics_shouldMapRepositoryEntitiesToDtos() {
        Plant plant = new Plant();
        plant.setPlantTypeId(2);
        EtcStatistic stat = new EtcStatistic();
        stat.setControllerId(4);
        stat.setDate(LocalDate.of(2026, 1, 2));
        stat.setEtc(3.5);
        stat.setTMin(10);
        stat.setTMax(20);
        stat.setTMean(15);
        stat.setRhMin(40);
        stat.setRhMax(80);
        stat.setPlant(plant);
        PlantType type = new PlantType();
        type.setId(2);
        type.setName("Strawberry");

        when(repository.findAll()).thenReturn(List.of(stat));
        when(cacheService.getPlantType(2)).thenReturn(type);

        var result = service.getEtcStatistics();
        EtcStatisticDto dto = result.getValue().getFirst();

        assertEquals(1, result.getValue().size());
        assertEquals(4, dto.getControllerId());
        assertEquals("Strawberry", dto.getPlantType());
        assertEquals(3.5, dto.getEtc());
        assertEquals(LocalDate.of(2026, 1, 2), dto.getDate());
    }

    @Test
    void getEtcStatistics_shouldReturnEmptyListWhenRepositoryIsEmpty() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.getEtcStatistics().getValue().isEmpty());
    }
}
