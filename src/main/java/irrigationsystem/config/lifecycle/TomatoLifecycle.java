package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

public class TomatoLifecycle extends Lifecycle {

    public TomatoLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(
                        0,
                        10,
                        "Кълнове",
                        "Поддържай почвата влажна, но не подгизнала. Осигури температура 20–25°C и добра светлина. Без торене на този етап.",
                        65.0,
                        80.0,
                        plantType,
                        4
                ),
                new GrowthPhase(
                        11,
                        30,
                        "Вегетативен растеж",
                        "Поливай редовно, тори с азотен тор. Ако се отглежда разсад – пресади към 25–30 ден. Приучи растението към външни условия.",
                        55.0,
                        75.0,
                        plantType,
                        6
                ),
                new GrowthPhase(
                        31,
                        50,
                        "Цъфтеж",
                        "Поливай умерено, избягвай пресушаване. Подхрани с фосфор и калий. Премахвай колтуци и подпомагай опрашването при нужда.",
                        60.0,
                        70.0,
                        plantType,
                        5
                ),
                new GrowthPhase(
                        51,
                        70,
                        "Плододаване",
                        "Поддържай постоянна влага. Подхрани с калиев тор. Подпори и премахвай излишни филизи за насочване на енергия към плодовете.",
                        60.0,
                        70.0,
                        plantType,
                        7
                ),
                new GrowthPhase(
                        71,
                        90,
                        "Узряване на плодовете",
                        "Намали поливането леко, за да избегнеш напукване. Спри торенето. Премахвай долни листа и прибирай плодовете при узряване.",
                        50.0,
                        60.0,
                        plantType,
                        4
                ),
                new GrowthPhase(
                        91,
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
