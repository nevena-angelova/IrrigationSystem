package irrigationsystem.config.lifecycle;

import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.PlantType;

import java.util.List;

public class PotatoLifecycle extends Lifecycle {

    public PotatoLifecycle(PlantType plantType) {
        super(plantType);
    }

    @Override
    public List<GrowthPhase> getPhases() {
        return List.of(
                new GrowthPhase(
                        0,
                        14,
                        "Кълняване и поникване",
                        "Поддържай почвата влажна и топла (15–20°C). Поливай леко. Не тори на този етап. Следи за равномерно поникване.",
                        70.0,
                        85.0,
                        plantType,
                        5
                ),
                new GrowthPhase(
                        15,
                        45,
                        "Вегетативен растеж",
                        "Поливай редовно, тори с азотен тор. Окучи растенията, когато достигнат 15–20 см височина. Следи за плевели и вредители.",
                        65.0,
                        80.0,
                        plantType,
                        7
                ),
                new GrowthPhase(
                        46,
                        60,
                        "Цъфтеж",
                        "Поливай равномерно, за да избегнеш стрес. Започни подхранване с фосфор и калий. Продължи окопаване при нужда.",
                        75.0,
                        85.0,
                        plantType,
                        6
                ),
                new GrowthPhase(
                        61,
                        90,
                        "Образуване на клубени",
                        "Поддържай умерена влажност на почвата. Не прекалявай с азот. Осигури добро проветряване и наблюдавай за мана и колорадски бръмбар.",
                        75.0,
                        80.0,
                        plantType,
                        8
                ),
                new GrowthPhase(
                        91,
                        120,
                        "Узряване на клубени",
                        "Намали поливането. Изчакай пожълтяване на листата. Спри торенето. Прибери клубените, след като растенията изсъхнат.",
                        70.0,
                        75.0,
                        plantType,
                        5
                ),
                new GrowthPhase(
                        121,
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
