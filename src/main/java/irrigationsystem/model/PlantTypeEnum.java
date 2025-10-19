package irrigationsystem.model;

public enum PlantTypeEnum {
    Tomato(1),
    Strawberry(2),
    Potato(3),
    Carrot(4);

    private final int value;

    PlantTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PlantTypeEnum getValue(int id) {
        for (PlantTypeEnum type : PlantTypeEnum.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Plant type value: " + id);
    }
}
