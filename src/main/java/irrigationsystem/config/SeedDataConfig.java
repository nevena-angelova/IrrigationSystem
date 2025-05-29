package irrigationsystem.config;

import irrigationsystem.model.*;
import irrigationsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final CropTypeRepository cropTypeRepository;
  private final PasswordEncoder passwordEncoder;
  private final MeasureTypeRepository measureTypeRepository;
  private final SensorTypeRepository sensorTypeRepository;

  @Override
  public void run(String... args) throws Exception {
    if (roleRepository.count() == 0) {

      Role userRole = Role.builder()
        .name(RoleEnum.USER.name())
        .build();

      Role adminRole = Role.builder()
        .name(RoleEnum.ADMIN.name())
        .build();

      roleRepository.save(userRole);
      roleRepository.save(adminRole);
    }

    if (userRepository.count() == 0) {

      User admin = new User();
      admin.setUsername("admin");
      admin.setFirstName("Nevena");
      admin.setLastName("Angelova");
      admin.setEmail("admin@gmail.com");
      admin.setPassword(passwordEncoder.encode("n@ng3l0va"));
      admin.setRole(roleRepository.findByName(RoleEnum.ADMIN.name()));

      if (admin.getId() == null) {
        admin.setCreated(LocalDateTime.now());
      }

      admin.setUpdated(LocalDateTime.now());

      userRepository.save(admin);
    }

    if (cropTypeRepository.count() == 0) {

      CropType tomato = new CropType();
      tomato.setName("Домат");

      CropType strawberry = new CropType();
      strawberry.setName("Ягода");

      CropType potato = new CropType();
      potato.setName("Картоф");

      CropType carrots = new CropType();
      carrots.setName("Морков");

      cropTypeRepository.saveAll(List.of(tomato, strawberry, potato, carrots));
    }

    if (measureTypeRepository.count() == 0 && sensorTypeRepository.count() == 0) {

      MeasureType temperature = new MeasureType();
      temperature.setName("Temperature");
      temperature.setUnit("Degree Celsius");

      MeasureType humidity = new MeasureType();
      humidity.setName("Humidity");
      humidity.setUnit("Percent");

      MeasureType pressure = new MeasureType();
      pressure.setName("Pressure");
      pressure.setUnit("Hectopascal");

      SensorType dht22 = new SensorType();
      dht22.setName("DHT22");
      dht22.addMeasureType(temperature);
      dht22.addMeasureType(humidity);

      SensorType tmp36 = new SensorType();
      tmp36.setName("TMP36");
      tmp36.addMeasureType(temperature);

      SensorType bmp180 = new SensorType();
      bmp180.setName("BMP180");
      bmp180.addMeasureType(pressure);

      sensorTypeRepository.saveAll(List.of(dht22, tmp36, bmp180));
    }
  }
}
