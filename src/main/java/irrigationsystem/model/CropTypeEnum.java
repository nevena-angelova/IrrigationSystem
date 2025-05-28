package irrigationsystem.model;

public enum CropTypeEnum {
    Tomato(1),
    Strawberry(2),
    Potato(3),
    Carrot(4);

    private final int value;

    CropTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CropTypeEnum getValue(int id) {
        for (CropTypeEnum type : CropTypeEnum.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid crop type value: " + id);
    }
}
