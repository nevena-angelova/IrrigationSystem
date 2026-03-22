package irrigationsystem.model;

public enum MeasureTypeEnum {
    Temperature(1),
    Humidity(2),
    Light(3),
    SoilMoisture(4);


    private final int value;

    MeasureTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
