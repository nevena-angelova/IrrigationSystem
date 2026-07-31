#include <WiFi.h>
#include "PubSubClient.h"
#include <DHT.h>
#include <Wire.h>
#include <BH1750.h>
#include <ArduinoJson.h>

// DHT settings

#define DHTPIN 4
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

// BH1750

BH1750 lightMeter;

const int lightSdaPin = 21;
const int lightSclPin = 22;

// Soil moisture sensor settings

const int numSoilSensors = 1;
const int soilPin = 32;

int soilSensorGPIOs[numSoilSensors] = { soilPin };

// Relay settings

const int numRelays = 1;
const int relayPin = 26;

int relayGPIOs[numRelays] = { relayPin };
bool relayOn[numRelays] = { false };
unsigned long relayEndTime[numRelays] = { 0 };

// WiFi settings

const char* ssid = "TP-LINK-Net";
const char* password = "St@r61pGalaxy";

// MQTT settings

const char* mqttServer = "192.168.0.101";
WiFiClient espClient;
PubSubClient mqttClient(espClient);

// Controller id

const long CONTROLLER_ID = 1;

// Publish timer

unsigned long lastMsg = 0;
//const long interval = 30UL * 60UL * 1000UL;  // 30 min
const long interval = 10UL * 1000UL;

// Handle incomming MQTT messages
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  // Payload to string
  String json;
  for (int i = 0; i < length; i++) {
    json += (char)payload[i];
  }
  Serial.println(json);

  // JSON parse
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, json);

  if (error) {
    Serial.print("JSON parse error: ");
    Serial.println(error.c_str());
    return;
  }

  int relayId = doc["areaNumber"];
  int duration = doc["irrigationDuration"];

  // Validate relay ID
  if (relayId <= 0 || relayId > numRelays) {
    Serial.println("Invalid relayId!");
    return;
  }

  // Incoming relay command

  if ((String(topic) == "garden/" + String(CONTROLLER_ID) + "/relay/" + relayId)) {
    relayId -= 1;
    //digitalWrite(relayGPIOs[relayId], LOW);
    relayOn[relayId] = true;
    relayEndTime[relayId] = millis() + (duration * 1000UL);

    Serial.print("Relay ");
    Serial.print(relayId);
    Serial.print(" ON for ");
    Serial.print(duration);
    Serial.println(" seconds");

    setShellyPlug(true);
  }
}

// Publish on/off mqtt message to Shelly Plug
void setShellyPlug(bool on) {

  StaticJsonDocument<200> doc;

  doc["method"] = "Switch.Set";

  JsonObject params = doc.createNestedObject("params");
  params["id"] = 0;
  params["on"] = on;

  String payload;
  serializeJson(doc, payload);

  mqttClient.publish(
    "shellyplusplugs-e465b8459c30/rpc",
    payload.c_str());
}

void setup() {
  Serial.begin(115200);
  dht.begin();

  // BH1750
  Wire.begin(lightSdaPin, lightSclPin);
  lightMeter.begin();

  for (int i = 0; i < numRelays; i++) {
    pinMode(relayGPIOs[i], OUTPUT);
    digitalWrite(relayGPIOs[i], HIGH);
  }

  // Connect to WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected");
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());

  mqttClient.setServer(mqttServer, 1883);
  mqttClient.setCallback(callback);
}

void loop() {

  // WiFi reconnect
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi disconnected. Reconnecting...");
    WiFi.reconnect();
    delay(500);
  }

  // MQTT connect
  while (!mqttClient.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (mqttClient.connect("ESP32Client")) {  // unique mqttClient name
      Serial.println("connected");

      // MQTT subscribe
      String topic = "garden/" + String(CONTROLLER_ID) + "/relay/+";
      mqttClient.subscribe(topic.c_str());
    } else {
      Serial.print("failed, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" try again in 0.5 seconds");
      delay(500);
    }
  }
  mqttClient.loop();

  for (int i = 0; i < numRelays; i++) {
    if (relayOn[i] && (millis() >= relayEndTime[i])) {
      // digitalWrite(relayGPIOs[i], HIGH);
      relayOn[i] = false;
      Serial.print("Relay ");
      Serial.print(i);
      Serial.println(" OFF ");

      setShellyPlug(false);
    }
  }

  // MQTT publish data in 30 sec
  unsigned long now = millis();
  if (now - lastMsg >= interval) {
    lastMsg = now;

    float temperature = dht.readTemperature();
    float humidity = dht.readHumidity();

    if (isnan(temperature) || isnan(humidity)) {
      Serial.println("Failed to read from DHT sensor!");
      return;
    }

    float lux = lightMeter.readLightLevel();

    if (isnan(lux)) {
      Serial.println("Failed to read from BH1750 sensor!");
      return;
    }

    // Soil moisture sensor
    // ADC convert (0 V - 3.3 V) to (0 - 4095)
    // Calibrated soil moisture sensor values
    int dryValue = 2200;
    int wetValue = 200;

    int soilPercents[numSoilSensors];
    int soilValues[numSoilSensors];

    for (int i = 0; i < numSoilSensors; i++) {
      int soilValue = analogRead(soilSensorGPIOs[i]);

      // map values to percents (0 - 100)
      soilPercents[i] = map(soilValue, dryValue, wetValue, 0, 100);
      soilPercents[i] = constrain(soilPercents[i], 0, 100);

      soilValues[i] = soilValue;
    }

    // Create ne Json object
    StaticJsonDocument<200> doc;
    doc["controllerId"] = CONTROLLER_ID;
    doc["temperature"] = temperature;
    doc["humidity"] = humidity;
    doc["light"] = lux;

    JsonArray soilArray = doc.createNestedArray("soilMoisture");
    JsonArray soilValuesArray = doc.createNestedArray("soilValue");

    for (int i = 0; i < numSoilSensors; i++) {
      soilArray.add(soilPercents[i]);
      soilValuesArray.add(soilValues[i]);
    }

    char buffer[200];
    size_t n = serializeJson(doc, buffer);

    // Send JSON payload
    String topic = "garden/" + String(CONTROLLER_ID) + "/sensors";

    mqttClient.publish(topic.c_str(), buffer, n);

    Serial.print("Published to topic: ");
    Serial.println(topic);
    Serial.print("Payload: ");
    Serial.println(buffer);
  }
}
