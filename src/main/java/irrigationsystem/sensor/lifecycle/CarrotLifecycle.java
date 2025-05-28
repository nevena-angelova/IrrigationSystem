package irrigationsystem.sensor.lifecycle;

import java.util.List;

public class CarrotLifecycle extends Lifecycle {
    @Override
    protected List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(0, 14,
                        new GrowthPhaseInfo(
                                "Кълняване",
                                "Поддържай почвата постоянно влажна. Оптимална температура за поникване е 10–20°C. Поникване отнема 7–14 дни. Не тори.",
                                70,
                                85
                        )
                ),
                new GrowthPhase(15, 35,
                        new GrowthPhaseInfo(
                                "Начален вегетативен растеж",
                                "Поливай редовно, особено при сухо време. Плеви внимателно. Разреди растенията, ако са прекалено гъсти.",
                                60,
                                80
                        )
                ),
                new GrowthPhase(36, 60,
                        new GrowthPhaseInfo(
                                "Развитие на кореноплод",
                                "Подхрани с фосфор и калий. Избягвай азот, за да не стимулираш листната маса. Поливай дълбоко, но не често.",
                                65,
                                75
                        )
                ),
                new GrowthPhase(61, 90,
                        new GrowthPhaseInfo(
                                "Нарастване на кореноплода",
                                "Наблюдавай за равномерно удебеляване. Поливай умерено, без да подгизва почвата. Следи за напукване или вредители като морковена муха.",
                                65,
                                70
                        )
                ),
                new GrowthPhase(91, 120,
                        new GrowthPhaseInfo(
                                "Узряване и прибиране",
                                "Намали поливането. Морковите са готови, когато достигнат характерен размер и цвят. Извади внимателно, за да не се чупят.",
                                60,
                                65
                        )
                ),
                new GrowthPhase(121, Integer.MAX_VALUE,
                        new GrowthPhaseInfo(
                                "Неизвестно",
                                "Няма информация.",
                                60,
                                60
                        )
                )
        );
    }
}
