import {StatisticService} from '../../service/statistic.service';
import {Component} from '@angular/core';
import {EtcStatistic} from '../../model/etc-statistic.model';
import {DecimalPipe, NgForOf} from '@angular/common';

@Component({
  selector: 'app-statistic',
  imports: [
    NgForOf,
    DecimalPipe
  ],
  templateUrl: './statistic.component.html',
  styleUrl: './statistic.component.css'
})
export class StatisticComponent {
  etcStatistics: EtcStatistic[] = [];
  constructor(private statisticService: StatisticService) {
  }

  ngOnInit(): void {
    this.statisticService.getPlantTypes().subscribe({
      next: (data) => this.etcStatistics = data,
      error: (err) => console.error('Error fetching Etc statistics:', err)
    });
  }
}
