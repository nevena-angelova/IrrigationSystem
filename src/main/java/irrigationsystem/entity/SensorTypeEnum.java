package irrigationsystem.entity;

public enum SensorTypeEnum {
    DHT22("DHT22"),
    BH1750("BH1750"),
    SOIL_MOISTURE("Capacitive Soil Moisture v1.2");

    private final String value;

    SensorTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
