package irrigationsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EtcStatisticDto {

    private int controllerId;

    private String plantType;

    private LocalDate date;

    private double tMin;

    private double tMax;

    private double tMean;

    private double rhMin;

    private double rhMax;

    private double etc;
}
