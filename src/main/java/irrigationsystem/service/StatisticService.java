package irrigationsystem.service;

import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.EtcStatisticDto;

import irrigationsystem.dto.ResponseDto;
import irrigationsystem.entity.EtcStatistic;
import irrigationsystem.repository.EtcStatisticRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatisticService {

    private final EtcStatisticRepository etcStatisticRepository;
    private final CacheService cacheService;

    public ResponseDto<List<EtcStatisticDto>> getEtcStatistics() {

        List<EtcStatistic> etcStatistics =  this.etcStatisticRepository.findAll();
        List<EtcStatisticDto> etcStatisticsDto = new ArrayList<>();

        etcStatistics.stream().forEach(etcStatistic -> {
            etcStatisticsDto.add(createEtcStatisticDto(etcStatistic));
        });

        return ResponseDto.<List<EtcStatisticDto>>builder().value(etcStatisticsDto).build();
    }

    private EtcStatisticDto createEtcStatisticDto(EtcStatistic etcStatistic){
        EtcStatisticDto  etcStatisticDto = new EtcStatisticDto();
        etcStatisticDto.setControllerId(etcStatistic.getControllerId());
        etcStatisticDto.setDate(etcStatistic.getDate());
        etcStatisticDto.setEtc(etcStatistic.getEtc());
        etcStatisticDto.setTMin(etcStatistic.getTMin());
        etcStatisticDto.setTMax(etcStatistic.getTMax());
        etcStatisticDto.setTMean(etcStatistic.getTMean());
        etcStatisticDto.setRhMin(etcStatistic.getRhMin());
        etcStatisticDto.setRhMax(etcStatistic.getRhMax());

        String plantType = cacheService.getPlantType(etcStatistic.getPlant().getPlantTypeId()).getName();

        etcStatisticDto.setPlantType(plantType);

        return etcStatisticDto;
    }
}
