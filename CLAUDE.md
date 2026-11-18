# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

IoT plant-irrigation system with three parts:
- **Backend** (repo root): Java 21, Spring Boot 3.4.5, Gradle, PostgreSQL, JPA, Spring Security + JWT, MQTT (Eclipse Paho), Caffeine cache, MapStruct, Lombok.
- **Frontend** (`client/irrigation-system/`): Angular 19 standalone components, Bootstrap / ng-bootstrap.
- **Firmware** (`esp32/sensor_data_provider.ino`): ESP32 Arduino sketch that publishes sensor readings and drives irrigation relays over MQTT.

## Commands

Backend (run from repo root):
```bash
./gradlew bootRun                     # start server on :8080
./gradlew build                       # compile + test
./gradlew test                        # all tests
./gradlew test --tests 'irrigationsystem.service.IrrigationServiceTest'            # one class
./gradlew test --tests 'irrigationsystem.service.IrrigationServiceTest.methodName' # one method
```
Tests are plain JUnit 5 + Mockito unit tests (`@ExtendWith(MockitoExtension.class)`); they do not need a database or MQTT broker. Running the app does: PostgreSQL at `localhost:5432/irrigation_system` and an MQTT broker at `mqtt.broker` (see `src/main/resources/application.properties`). Schema is managed by `ddl-auto=update`; there are no migrations.

Frontend (from `client/irrigation-system/`):
```bash
npm install
ng serve        # dev server; talks to http://localhost:8080/ (environment.development.ts)
ng build
ng test         # Karma + Jasmine
```

## Architecture

### MQTT data flow (the core loop)
1. ESP32 publishes JSON to `garden/{controllerId}/sensors`: `{controllerId, temperature, humidity, light, soilMoisture: [..]}`.
2. `mqtt/MqttSubscriber` subscribes to `garden/+/sensors` on `ApplicationReadyEvent` and calls `SensorDataService.saveSensorData`.
3. `SensorDataService.saveSensorData` maps values onto the controller's `Sensor` rows **by sensor order** (`findByControllerIdOrderByIdAsc`): the Nth soil-moisture sensor gets `soilMoisture[N]`. Each plant is tied to a soil-moisture sensor, so sensor ordering matters.
4. `IrrigationScheduler` (`@Scheduled`, intended daily cron at 05:30 UTC) calls `IrrigationService.processDailyIrrigation`:
   - aggregates last-24h controller metrics (tMin/tMax/tMean/rhMin/rhMax) and latest soil moisture per plant,
   - resolves the plant's `GrowthPhase` from days since planting (`CacheService.getGrowthPhase`),
   - computes daily crop evapotranspiration ETc (`calculator/EvapotranspirationCalculator`, FAO Penman-Monteith style, crop coefficient from the growth phase) and stores an `EtcStatistic` row,
   - accumulates ETc on the `Plant`; when soil moisture < phase `minSoilMoisture`, computes duration (`IrrigationCalculator.calculateIrrigationDuration`, **minutes**, from plant spacing in cm and emitter flow in L/h), publishes it, and resets accumulated ETc to 0.
5. `MqttService.irrigate` publishes `{areaNumber, irrigationDuration}` to `garden/{controllerId}/relay/{areaNumber}`; the ESP32 subscribes to `garden/{CONTROLLER_ID}/relay/+`.

MQTT topic formats and payload field names are a contract between `MqttSubscriber`/`MqttService` and the `.ino` sketch; change both sides together.

### Plant types: lifecycles and analyzers
Supported crops (`PlantTypeEnum`): Tomato, Strawberry, Potato, Carrot. Adding a crop touches several places:
- `config/lifecycle/*Lifecycle` – defines the crop's `GrowthPhase`s (day ranges, crop coefficient, soil-moisture limits); seeded by `config/SeedDataConfig` (a `CommandLineRunner` that only seeds when tables are empty).
- `analyzer/*Analyzer` + `AnalyzerFactory` – produce per-plant `ReportDto` warnings used by `PlantService` for the plant dashboard.
- Warning/phase text keys come from `messages_bg.properties` (Bulgarian; a copy exists in `src/test/resources` for analyzer tests).

### Other backend notes
- `cache/CacheService` caches all `PlantType`s and `GrowthPhase`s (Caffeine, 30 min) and does lookups in memory; reference data changes need cache awareness.
- Auth: `AuthenticationController` (`/api/auth/*`) issues JWTs (`jwt/JwtUtil`, `JwtFilter`); everything else under `/api/**` requires a token (`config/SecurityConfig`, which also configures CORS). Swagger UI is via springdoc.
- Layering: `controller` → `service` → `repository` (Spring Data JPA, several native/projection queries in `SensorDataRepository` returning `model/*` projections); `dto` + `mapper/Mapper` (MapStruct) for API shapes.

### Frontend
Routes in `app.routes.ts`: `authentication`, `plant`, `statistic` (last two behind `authGuard`). `service/token.interceptor.ts` attaches the JWT; services build URLs from `environment.apiUrl` + `api/...`.
