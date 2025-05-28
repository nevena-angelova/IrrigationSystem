package irrigationsystem.sensor.lifecycle;

import java.util.List;

public class TomatoLifecycle extends Lifecycle {

    @Override
    protected List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(0, 10,
                        new GrowthPhaseInfo(
                                "Кълнове",
                                "Поддържай почвата влажна, но не подгизнала. Осигури температура 20–25°C и добра светлина. Без торене на този етап.",
                                65,
                                80
                        )),
                new GrowthPhase(11, 30,
                        new GrowthPhaseInfo(
                                "Вегетативен растеж",
                                "Поливай редовно, тори с азотен тор. Ако се отглежда разсад – пресади към 25–30 ден. Приучи растението към външни условия.",
                                55,
                                75
                        )),
                new GrowthPhase(31, 50,
                        new GrowthPhaseInfo(
                                "Цъфтеж",
                                "Поливай умерено, избягвай пресушаване. Подхрани с фосфор и калий. Премахвай колтуци и подпомагай опрашването при нужда.",
                                60,
                                70
                        )),
                new GrowthPhase(51, 70,
                        new GrowthPhaseInfo(
                                "Плододаване",
                                "Поддържай постоянна влага. Подхрани с калиев тор. Подпори и премахвай излишни филизи за насочване на енергия към плодовете.",
                                60,
                                70
                        )),
                new GrowthPhase(71, 90,
                        new GrowthPhaseInfo(
                                "Узряване на плодовете",
                                "Намали поливането леко, за да избегнеш напукване. Спри торенето. Премахвай долни листа и прибирай плодовете при узряване.",
                                50,
                                60
                        )),
                new GrowthPhase(91, Integer.MAX_VALUE,
                        new GrowthPhaseInfo(
                                "Неизвестно",
                                "Няма информация.",
                                60,
                                60
                        ))
        );
    }
}