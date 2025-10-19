package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

public class CarrotLifecycle extends Lifecycle {

    public CarrotLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(
                        0,
                        14,
                        "Кълняване",
                        "Поддържай почвата постоянно влажна. Оптимална температура за поникване е 10–20°C. Поникване отнема 7–14 дни. Не тори.",
                        70.0,
                        85.0,
                        plantType,
                        5
                ),
                new GrowthPhase(
                        15,
                        35,
                        "Начален вегетативен растеж",
                        "Поливай редовно, особено при сухо време. Плеви внимателно. Разреди растенията, ако са прекалено гъсти.",
                        60.0,
                        80.0,
                        plantType,
                        6
                ),
                new GrowthPhase(
                        36,
                        60,
                        "Развитие на кореноплод",
                        "Подхрани с фосфор и калий. Избягвай азот, за да не стимулираш листната маса. Поливай дълбоко, но не често.",
                        65.0,
                        75.0,
                        plantType,
                        7
                ),
                new GrowthPhase(
                        61,
                        90,
                        "Нарастване на кореноплода",
                        "Наблюдавай за равномерно удебеляване. Поливай умерено, без да подгизва почвата. Следи за напукване или вредители като морковена муха.",
                        65.0,
                        70.0,
                        plantType,
                        6
                ),
                new GrowthPhase(
                        91,
                        120,
                        "Узряване и прибиране",
                        "Намали поливането. Морковите са готови, когато достигнат характерен размер и цвят. Извади внимателно, за да не се чупят.",
                        60.0,
                        65.0,
                        plantType,
                        4
                ),
                new GrowthPhase(
                        121,
                        Integer.MAX_VALUE,
                        "Неизвестно",
                        "Няма информация.",
                        60.0,
                        60.0,
                        plantType,
                        2
                )
        );
    }
}
