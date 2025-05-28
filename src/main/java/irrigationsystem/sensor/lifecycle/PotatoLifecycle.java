package irrigationsystem.sensor.lifecycle;

import java.util.List;

public class PotatoLifecycle extends Lifecycle {

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(0, 14,
                        new GrowthPhaseInfo(
                                "Кълняване и поникване",
                                "Поддържай почвата влажна и топла (15–20°C). Поливай леко. Не тори на този етап. Следи за равномерно поникване.",
                                70,
                                85
                        )),
                new GrowthPhase(15, 45,
                        new GrowthPhaseInfo(
                                "Вегетативен растеж",
                                "Поливай редовно, тори с азотен тор. Окучи растенията, когато достигнат 15–20 см височина. Следи за плевели и вредители.",
                                65,
                                80
                        )),
                new GrowthPhase(46, 60,
                        new GrowthPhaseInfo(
                                "Цъфтеж",
                                "Поливай равномерно, за да избегнеш стрес. Започни подхранване с фосфор и калий. Продължи окопаване при нужда.",
                                75,
                                85
                        )),
                new GrowthPhase(61, 90,
                        new GrowthPhaseInfo(
                                "Образуване на клубени",
                                "Поддържай умерена влажност на почвата. Не прекалявай с азот. Осигури добро проветряване и наблюдавай за мана и колорадски бръмбар.",
                                75,
                                80
                        )),
                new GrowthPhase(91, 120,
                        new GrowthPhaseInfo(
                                "Узряване на клубени",
                                "Намали поливането. Изчакай пожълтяване на листата. Спри торенето. Прибери клубените, след като растенията изсъхнат.",
                                70,
                                75
                        )),
                new GrowthPhase(121, Integer.MAX_VALUE,
                        new GrowthPhaseInfo(
                                "Неизвестно",
                                "Няма информация.",
                                60,
                                60
                        ))
        );
    }
}
