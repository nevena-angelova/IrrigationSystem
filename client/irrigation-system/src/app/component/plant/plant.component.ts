import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {PlantService} from '../../service/plant.service';
import {FormsModule} from '@angular/forms';
import {DatePipe, DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Plant} from '../../model/plant.model';
import {CreatePlant} from '../../model/create-plant.model';
import {Report} from '../../model/report.model';
import {WebSocketService} from '../../service/websocket.service';
import {Subscription} from 'rxjs';
import {RingComponent} from '../../shared/ring/ring.component';

@Component({
  selector: 'app-plant',
  imports: [
    FormsModule,
    NgForOf,
    DatePipe,
    NgIf,
    RingComponent,
    DecimalPipe
  ],
  templateUrl: './plant.component.html',
  styleUrl: './plant.component.css'
})
export class PlantComponent implements OnInit {
  plantTypes: any[] = [];
  selectedPlantType: number | undefined;
  selectedDate: Date = new Date();
  plants: Plant[] = [];
  reportSubscription: Subscription | undefined;
  report: Report = new Report();

  controllerId: number = 1;
  distanceX: number = 0;
  distanceY: number = 0;
  emitterFlow: number = 0;


  constructor(private plantService: PlantService, private cdRef: ChangeDetectorRef, private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {
    this.plantService.getPlantTypes().subscribe({
      next: (data) => this.plantTypes = data,
      error: (err) => console.error('Error fetching Plant types:', err)
    });

    this.getPlants();


    // Subscribe to messages from the WebSocket
    this.reportSubscription = this.webSocketService.getMessages().subscribe(
      (report) => {
        this.report = report;
        const plant = this.plants.find(p => p.id === report.plantId);
        if (plant) {
          plant.report = report;
          this.cdRef.detectChanges(); // Optional if NgZone is used in WebSocketService
        }
      }
    );
  }

  getPlants() {
    this.plantService.getPlants().subscribe({
      next: (data) => {
        this.plants = data;
        this.plants.forEach(plant => this.getPlantIcon(plant))
        this.cdRef.detectChanges();
      },
      error: (err) => console.error('Error fetching Plant types:', err)
    });
  }

  createPlant() {
    if (this.selectedPlantType === undefined) {
      console.error('No Plant type selected');
      return;
    }

    const plant: CreatePlant = {
      plantTypeId: this.selectedPlantType,
      plantingDate: this.selectedDate,
      controllerId: this.controllerId,
      distanceX: this.distanceX,
      distanceY: this.distanceY,
      emitterFlow: this.emitterFlow
    };

    this.plantService.createPlant(plant).subscribe({
      next: () => this.getPlants(),
      error: (err) => console.error('Error creating Plant:', err)
    });
  }

  hasWarnings(plant: Plant) {
    return plant.report?.warnings.length > 0;
  }

  getTemperature(plant: Plant) {
    return plant.report.temperature;
  }

  getHumidity(plant: Plant) {
    return plant.report.humidity;
  }

  getLight(plant: Plant) {
    return plant.report.light;
  }

  getSoilMoisture(plant: Plant) {
    return plant.report.soilMoisture;
  }

  getMinSoilMoisture(plant: Plant) {
    return plant.report.minSoilMoisture;
  }

  getMaxSoilMoisture(plant: Plant) {
    return plant.report.maxSoilMoisture;
  }

  hasReport(plant: Plant) {
    return plant.report !== undefined;
  }

  getPlantIcon(plant: Plant) {
    switch (plant.plantType.name) {
      case 'Домат':
        plant.icon = '🍅';
        break;
      case 'Ягода':
        plant.icon = '🍓';
        break
      case 'Морков':
        plant.icon = '🥕';
        break;
      case 'Картоф':
        plant.icon = '🥔'
        break;
      default:
        plant.icon = '';
        break;
    }
  }
}
