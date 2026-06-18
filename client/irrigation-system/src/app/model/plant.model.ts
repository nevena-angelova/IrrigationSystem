import {PlantType} from './plant-type.model';
import {Report} from './report.model';

export interface Plant {
  id: number;
  plantingDate: Date;
  plantType: PlantType;
  controllerId: number;
  distanceX: number;
  distanceY: number;
  emitterFlow: number;
  relayNumber: number;
  icon: string;
  report: Report;
}
