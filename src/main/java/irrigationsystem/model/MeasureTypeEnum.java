package irrigationsystem.model;

public enum MeasureTypeEnum {
    Temperature(1),
    Humidity(2),
    SoilMoisture(3);

    private final int value;

    MeasureTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
