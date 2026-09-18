package irrigationsystem.service;

import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.CreatePlantDto;
import irrigationsystem.entity.Controller;
import irrigationsystem.entity.SensorType;
import irrigationsystem.entity.User;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.mqtt.MqttPublisher;
import irrigationsystem.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {
    @Mock PlantRepository plantRepository;
    @Mock Mapper mapper;
    @Mock SensorTypeRepository sensorTypeRepository;
    @Mock ControllerRepository controllerRepository;
    @Mock CacheService cacheService;
    @Mock SensorRepository sensorRepository;
    @InjectMocks PlantService service;

    @AfterEach
    void clearSecurityContext() { SecurityContextHolder.clearContext(); }

    @Test
    void getPlantTypes_shouldMapCachedPlantTypes() {
        when(cacheService.plantTypes()).thenReturn(List.of());
        when(mapper.toPlantTypeDtoList(List.of())).thenReturn(List.of());

        var result = service.getPlantTypes();

        assertNotNull(result.getValue());
        assertTrue(result.getValue().isEmpty());
    }

    @Test
    void createPlant_shouldReturnErrorWithoutAuthenticatedUser() {
        SecurityContextHolder.clearContext();

        var result = service.createPlant(mock(CreatePlantDto.class));

        assertTrue(result.hasErrors());
        assertEquals("Invalid authentication.", result.getErrorMessage().orElseThrow());
        verifyNoInteractions(plantRepository, controllerRepository, sensorRepository);
    }

    @Test
    void createPlant_shouldSavePlantAndSoilSensorForAuthenticatedUser() {
        User user = new User();
        user.setId(11L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));

        CreatePlantDto dto = mock(CreatePlantDto.class);
        when(dto.getPlantTypeId()).thenReturn(1);
        when(dto.getPlantingDate()).thenReturn(LocalDate.of(2026, 1, 1));
        when(dto.getDistanceX()).thenReturn(50);
        when(dto.getDistanceY()).thenReturn(40);
        when(dto.getEmitterFlow()).thenReturn(2.0);

        Controller controller = mock(Controller.class);
        when(controller.getId()).thenReturn(7);
        when(controllerRepository.getControllerByUserId(11L)).thenReturn(controller);
        when(plantRepository.getPlantCount(7)).thenReturn(2);
        when(plantRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SensorType soilType = new SensorType();
        soilType.setName("Capacitive Soil Moisture v1.2");
        when(sensorTypeRepository.findByNameIn(anyList())).thenReturn(List.of(soilType));

        var result = service.createPlant(dto);

        assertFalse(result.hasErrors());
        assertEquals("Plant created successfully", result.getValue());
        verify(plantRepository).save(argThat(p -> p.getPlantTypeId() == 1 && p.getAreaNumber() == 3));
        verify(sensorRepository).save(any());
    }
}
