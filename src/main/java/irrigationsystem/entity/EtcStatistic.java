package irrigationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "etc_statistic")
public class EtcStatistic {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double tMin;

    @Column(nullable = false)
    private double tMax;

    @Column(nullable = false)
    private double tMean;

    @Column(nullable = false)
    private double rhMin;

    @Column(nullable = false)
    private double rhMax;

    @Column(nullable = false)
    private double etc;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @Column(name = "controller_id")
    private int controllerId;

    @ManyToOne
    @JoinColumn(name = "controller_id", nullable = false, insertable = false, updatable = false)
    private Controller plantType;
}
