package irrigationsystem.config;

import irrigationsystem.config.lifecycle.PotatoLifecycle;
import irrigationsystem.model.*;
import irrigationsystem.repository.*;
import irrigationsystem.config.lifecycle.CarrotLifecycle;
import irrigationsystem.config.lifecycle.StrawberryLifecycle;
import irrigationsystem.config.lifecycle.TomatoLifecycle;
import org.springframework.boot.CommandLineRunner;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PlantTypeRepository plantTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final MeasureTypeRepository measureTypeRepository;
    private final SensorTypeRepository sensorTypeRepository;
    private final DeviceRepository deviceRepository;
    private final SensorRepository sensorRepository;
    private final GrowthPhaseRepository growthPhaseRepository;

    @Override
    public void run(String... args) throws Exception {

        seedMeasureAndSensorTypes();
        seedAdminUserWithDeviceAndSensors();
        seedPlantsAndGrowthPhases();
    }

    private void seedMeasureAndSensorTypes() {
        if (measureTypeRepository.count() > 0 || sensorTypeRepository.count() > 0) return;

        MeasureType temperature = createMeasure("Temperature", "°C");
        MeasureType humidity = createMeasure("Humidity", "%");
        MeasureType light = createMeasure("Light", "lx");
        MeasureType soilMoisture = createMeasure("SoilMoisture", "%");

        SensorType dht22 = createSensorType("DHT22", List.of(temperature, humidity));
        SensorType bh1750 = createSensorType("BH1750", List.of(light));
        SensorType capacitiveSoilMoisture = createSensorType("Capacitive Soil Moisture v1.2", List.of(soilMoisture));

        sensorTypeRepository.saveAll(List.of(dht22, bh1750, capacitiveSoilMoisture));
    }

    private void seedAdminUserWithDeviceAndSensors() {
        if (roleRepository.count() > 0 || userRepository.count() > 0 || deviceRepository.count() > 0 || sensorRepository.count() > 0)
            return;

        Role adminRole = Role.builder()
                .name(RoleEnum.ADMIN.name())
                .build();

        roleRepository.save(adminRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setFirstName("Nevena");
        admin.setLastName("Angelova");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("n@ng3l0va"));
        admin.setRole(adminRole);

        if (admin.getId() == null) {
            admin.setCreated(LocalDateTime.now());
        }

        admin.setUpdated(LocalDateTime.now());

        admin = userRepository.save(admin);

        /*
         Create device for admin user
         */

        Device device = new Device();
        device.setName("ESP32-Admin");
        device.setUser(admin);

        device = deviceRepository.save(device);

        admin.getDevices().add(device);

        userRepository.save(admin);

        /*
         Add sensors to a device
         */

        List<SensorType> sensorTypes = sensorTypeRepository.findAll();
        List<Sensor> sensors = new ArrayList<>();

        for (var sensorType : sensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(sensorType);
            sensor.setDevice(device);
            sensors.add(sensor);
        }

        sensorRepository.saveAll(sensors);
    }

    private void seedPlantsAndGrowthPhases() {
        if (plantTypeRepository.count() > 0 || growthPhaseRepository.count() > 0) return;

        PlantType tomato = createPlant("Домат");
        PlantType strawberry = createPlant("Ягода");
        PlantType potato = createPlant("Картоф");
        PlantType carrot = createPlant("Морков");

        plantTypeRepository.saveAll(List.of(tomato, strawberry, potato, carrot));

        List<GrowthPhase> growthPhases = new ArrayList<>();

        growthPhases.addAll(new StrawberryLifecycle(strawberry).getPhases());
        growthPhases.addAll(new TomatoLifecycle(tomato).getPhases());
        growthPhases.addAll(new PotatoLifecycle(potato).getPhases()); // ✅ FIXED
        growthPhases.addAll(new CarrotLifecycle(carrot).getPhases());

        growthPhaseRepository.saveAll(growthPhases);
    }

    private MeasureType createMeasure(String name, String unit) {
        MeasureType measure = new MeasureType();
        measure.setName(name);
        measure.setUnit(unit);
        return measure;
    }

    private SensorType createSensorType(String name, List<MeasureType> measures) {
        SensorType sensorType = new SensorType();
        sensorType.setName(name);
        measures.forEach(sensorType::addMeasureType);
        return sensorType;
    }

    private PlantType createPlant(String name) {
        PlantType plant = new PlantType();
        plant.setName(name);
        return plant;
    }
}
