package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

public class StrawberryLifecycle extends Lifecycle {

    public StrawberryLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(
                        0,
                        21,
                        "Вкореняване",
                        "Поливай често и умерено, поддържай почвата влажна. Избягвай торене. Мулчирай и плеви внимателно, за да не се наранят младите корени.",
                        70.0,
                        85.0,
                        plantType,
                        5
                ),
                new GrowthPhase(
                        22,
                        40,
                        "Вегетативен растеж",
                        "Подхрани с азотен тор, поливай редовно. Премахвай сухи листа, мулчирай за задържане на влага и следи за вредители.",
                        60.0,
                        75.0,
                        plantType,
                        6
                ),
                new GrowthPhase(
                        41,
                        60,
                        "Цъфтеж",
                        "Поливай умерено, без да мокриш цветовете. Подхрани с фосфор и калий. Опрашването може да се подпомогне. Следи за брашнеста мана и сиво гниене.",
                        65.0,
                        75.0,
                        plantType,
                        5
                ),
                new GrowthPhase(
                        61,
                        80,
                        "Плододаване",
                        "Поддържай постоянна влага, тори с калиев тор. Премахвай столони, ако не се използват за размножаване. Бери редовно зрели плодове.",
                        50.0,
                        65.0,
                        plantType,
                        7
                ),
                new GrowthPhase(
                        81,
                        100,
                        "Следплододаване",
                        "Премахни сухи листа и столони. Тори с азот за възстановяване. Поддържай леко поливане и почисти мястото за предотвратяване на болести.",
                        50.0,
                        60.0,
                        plantType,
                        4
                ),
                new GrowthPhase(
                        101,
                        Integer.MAX_VALUE,
                        "Неизвестно",
                        "Няма информация.",
                        60.0,
                        60.0,
                        plantType,
                        3
                )
        );
    }
}
