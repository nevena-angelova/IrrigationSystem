package irrigationsystem.sensor.lifecycle;

import java.util.List;

public class StrawberryLifecycle extends Lifecycle {

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(0, 21,
                        new GrowthPhaseInfo(
                                "Вкореняване",
                                "Поливай често и умерено, поддържай почвата влажна. Избягвай торене. Мулчирай и плеви внимателно, за да не се наранят младите корени.",
                                70,
                                85
                        )),
                new GrowthPhase(22, 40,
                        new GrowthPhaseInfo(
                                "Вегетативен растеж",
                                "Подхрани с азотен тор, поливай редовно. Премахвай сухи листа, мулчирай за задържане на влага и следи за вредители.",
                                60,
                                75
                        )),
                new GrowthPhase(41, 60,
                        new GrowthPhaseInfo(
                                "Цъфтеж",
                                "Поливай умерено, без да мокриш цветовете. Подхрани с фосфор и калий. Опрашването може да се подпомогне. Следи за брашнеста мана и сиво гниене.",
                                65,
                                75
                        )),
                new GrowthPhase(61, 80,
                        new GrowthPhaseInfo(
                                "Плододаване",
                                "Поддържай постоянна влага, тори с калиев тор. Премахвай столони, ако не се използват за размножаване. Бери редовно зрели плодове.",
                                50,
                                65
                        )),
                new GrowthPhase(81, 100,
                        new GrowthPhaseInfo(
                                "Следплододаване",
                                "Премахни сухи листа и столони. Тори с азот за възстановяване. Поддържай леко поливане и почисти мястото за предотвратяване на болести.",
                                50,
                                60
                        )),
                new GrowthPhase(101, Integer.MAX_VALUE,
                        new GrowthPhaseInfo(
                                "Неизвестно",
                                "Няма информация.",
                                60,
                                60
                        ))
        );
    }
}
