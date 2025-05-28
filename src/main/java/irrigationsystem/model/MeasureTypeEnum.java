package irrigationsystem.model;

public enum MeasureTypeEnum {
    Temperature(1),
    Humidity(2),
    Pressure(3);

    private final int value;

    private MeasureTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
