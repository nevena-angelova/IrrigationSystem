import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {CropService} from '../../service/crop.service';
import {FormsModule} from '@angular/forms';
import {DatePipe, DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Crop} from '../../model/crop.model';
import {CreateCrop} from '../../model/create-crop.model';
import {Report} from '../../model/report.model';
import {WebSocketService} from '../../service/websocket.service';
import {Subscription} from 'rxjs';
import {RingComponent} from '../../shared/ring/ring.component';

@Component({
  selector: 'app-crop',
  imports: [
    FormsModule,
    NgForOf,
    DatePipe,
    NgIf,
    RingComponent,
    DecimalPipe
  ],
  templateUrl: './crop.component.html',
  styleUrl: './crop.component.css'
})
export class CropComponent implements OnInit {
  cropTypes: any[] = [];
  selectedCropType: number | undefined;
  selectedDate: Date = new Date();
  crops: Crop[] = [];
  reportSubscription: Subscription | undefined;
  report: Report = {cropId: 0, needsIrrigation: false, warnings: [], humidity: 0, maxHumidity: 0, minHumidity: 0};

  constructor(private cropService: CropService, private cdRef: ChangeDetectorRef, private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {
    this.cropService.getCropTypes().subscribe({
      next: (data) => this.cropTypes = data,
      error: (err) => console.error('Error fetching crop types:', err)
    });

    this.getCrops();


    // Subscribe to messages from the WebSocket
    this.reportSubscription = this.webSocketService.getMessages().subscribe(
      (report) => {
        this.report = report;
        const crop = this.crops.find(c => c.id === report.cropId);
        if (crop) {
          crop.report = report;
          this.cdRef.detectChanges(); // Optional if NgZone is used in WebSocketService
        }
      }
    );
  }

  getCrops() {
    this.cropService.getCrops().subscribe({
      next: (data) => {
        this.crops = data;
        this.crops.forEach(crop => this.getCropIcon(crop))
        this.cdRef.detectChanges();
      },
      error: (err) => console.error('Error fetching crop types:', err)
    });
  }

  createCrop() {
    if (this.selectedCropType === undefined) {
      console.error('No crop type selected');
      return;
    }

    const crop: CreateCrop = {
      cropTypeId: this.selectedCropType,
      plantingDate: this.selectedDate,
    };

    this.cropService.createCrop(crop).subscribe({
      next: () => this.getCrops(),
      error: (err) => console.error('Error creating crop:', err)
    });
  }

  needsIrrigation(crop: Crop) {
    return crop.report?.needsIrrigation;
  }

  hasWarnings(crop: Crop) {
    return crop.report?.warnings.length > 0;
  }

  getHumidity(crop: Crop) {
    return crop.report.humidity;
  }

  getMinHumidity(crop: Crop) {
    return crop.report.minHumidity;
  }

  getMaxHumidity(crop: Crop) {
    return crop.report.maxHumidity;
  }

  hasReport(crop: Crop) {
    return crop.report !== undefined;
  }

  getCropIcon(crop: Crop) {
    switch (crop.cropType.name) {
      case 'Домат':
        crop.icon = '🍅';
        break;
      case 'Ягода':
        crop.icon = '🍓';
        break
      case 'Морков':
        crop.icon = '🥕';
        break;
      case 'Картоф':
        crop.icon = '🥔'
        break;
      default:
        crop.icon = '';
        break;
    }
  }
}
