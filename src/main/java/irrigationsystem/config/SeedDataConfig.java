package irrigationsystem.config;

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
    private final RelayRepository relayRepository;
    private final GrowthPhaseRepository growthPhaseRepository;

    @Override
    public void run(String... args) throws Exception {

        seedAdminUserWithDeviceAndSensors();

        seedMeasureAndSensorTypes();

        seedPlantsAndGrowthPhases();

    }

    private void seedAdminUserWithDeviceAndSensors() {
        if (roleRepository.count() > 0 || userRepository.count() > 0 || deviceRepository.count() > 0 || sensorRepository.count() > 0 || relayRepository.count() > 0)
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

        // Create device for admin user

        Device device = new Device();
        device.setName("ESP32-Admin");
        device.setUser(admin);

        device = deviceRepository.save(device);

        admin.getDevices().add(device);

        userRepository.save(admin);

        // Add sensors to device

        List<SensorType> sensorTypes = sensorTypeRepository.findByNameIn(List.of("DHT22", "BMP180"));
        List<Sensor> sensors = new ArrayList<>();

        for (var sensorType : sensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(sensorType);
            sensor.setDevice(device);
            sensors.add(sensor);
        }

        sensorRepository.saveAll(sensors);

        List<Relay> relays = new ArrayList<>();

        Relay relay = new Relay();
        relay.setUsed(false);
        relay.setDevice(device);

        relays.add(relay);

        relayRepository.saveAll(relays);
    }

    private void seedMeasureAndSensorTypes() {
        if (measureTypeRepository.count() > 0 || sensorTypeRepository.count() > 0) return;

        MeasureType temperature = new MeasureType();
        temperature.setName("Temperature");
        temperature.setUnit("°C");

        MeasureType soilMoisture = new MeasureType();
        soilMoisture.setName("Humidity");
        soilMoisture.setUnit("%");

        MeasureType pressure = new MeasureType();
        pressure.setName("SoilMoisture");
        pressure.setUnit("%");

        SensorType dht22 = new SensorType();
        dht22.setName("DHT22");
        dht22.addMeasureType(temperature);
        dht22.addMeasureType(soilMoisture);

        SensorType tmp36 = new SensorType();
        tmp36.setName("TMP36");
        tmp36.addMeasureType(temperature);

        SensorType bmp180 = new SensorType();
        bmp180.setName("BMP180");
        bmp180.addMeasureType(pressure);

        sensorTypeRepository.saveAll(List.of(dht22, tmp36, bmp180));
    }

    private void seedPlantsAndGrowthPhases() {
        if (plantTypeRepository.count() > 0 || growthPhaseRepository.count() > 0) return;

        PlantType tomato = new PlantType();
        tomato.setName("Домат");

        PlantType strawberry = new PlantType();
        strawberry.setName("Ягода");

        PlantType potato = new PlantType();
        potato.setName("Картоф");

        PlantType carrots = new PlantType();
        carrots.setName("Морков");

        plantTypeRepository.saveAll(List.of(tomato, strawberry, potato, carrots));

        List<GrowthPhase> strawberryPhases = new StrawberryLifecycle(strawberry).getPhases();
        List<GrowthPhase> tomatoPhases = new TomatoLifecycle(tomato).getPhases();
        List<GrowthPhase> potatoPhases = new TomatoLifecycle(potato).getPhases();
        List<GrowthPhase> carrotPhases = new CarrotLifecycle(carrots).getPhases();

        List<GrowthPhase> growthPhases = new ArrayList<>() {
            {
                addAll(strawberryPhases);
                addAll(tomatoPhases);
                addAll(potatoPhases);
                addAll(carrotPhases);
            }
        };

        growthPhaseRepository.saveAll(growthPhases);
    }
}
