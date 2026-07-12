package irrigationsystem.config;

import irrigationsystem.config.lifecycle.PotatoLifecycle;
import irrigationsystem.entity.*;
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
    private final ControllerRepository controllerRepository;
    private final SensorRepository sensorRepository;
    private final GrowthPhaseRepository growthPhaseRepository;

    @Override
    public void run(String... args) {
        seedMeasureAndSensorTypes();
        seedAdminUserWithDeviceAndSensors();
        seedPlantsAndGrowthPhases();
    }

    private void seedMeasureAndSensorTypes() {
        if (measureTypeRepository.count() > 0 || sensorTypeRepository.count() > 0) return;

        MeasureType temperature = createMeasure(MeasureTypeEnum.Temperature.name(), "°C");
        MeasureType humidity = createMeasure(MeasureTypeEnum.Humidity.name(), "%");
        MeasureType light = createMeasure(MeasureTypeEnum.Light.name(), "lx");
        MeasureType soilMoisture = createMeasure(MeasureTypeEnum.SoilMoisture.name(), "%");

        SensorType dht22 = createSensorType(SensorTypeEnum.DHT22.getValue(), List.of(temperature, humidity));
        SensorType bh1750 = createSensorType(SensorTypeEnum.BH1750.getValue(), List.of(light));
        SensorType capacitiveSoilMoisture = createSensorType(SensorTypeEnum.SOIL_MOISTURE.getValue(), List.of(soilMoisture));

        sensorTypeRepository.saveAll(List.of(dht22, bh1750, capacitiveSoilMoisture));
    }

    private void seedAdminUserWithDeviceAndSensors() {
        if (roleRepository.count() > 0 || userRepository.count() > 0 || controllerRepository.count() > 0 || sensorRepository.count() > 0)
            return;

        Role userRole = Role.builder()
            .name(RoleEnum.ADMIN.name())
            .build();

        Role adminRole = Role.builder()
                .name(RoleEnum.ADMIN.name())
                .build();

        roleRepository.saveAll(List.of(userRole, adminRole));

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
         Add controller for admin user
         */

        Controller controller = new Controller();
        controller.setNumber(1);
        controller.setLatitude(42.697778); // Sofia
        controller.setAltitude(550);
        controller.setUser(admin);

        controller = controllerRepository.save(controller);

        admin.getControllers().add(controller);

        userRepository.save(admin);

        /*
         Add default sensors to a controller
         */

        List<SensorType> defaultSensorTypes = sensorTypeRepository.findByNameIn(List.of(SensorTypeEnum.DHT22.getValue(), SensorTypeEnum.BH1750.getValue()));
        List<Sensor> sensors = new ArrayList<>();

        for (var sensorType : defaultSensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(sensorType);
            sensor.setController(controller);
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
        growthPhases.addAll(new PotatoLifecycle(potato).getPhases());
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
