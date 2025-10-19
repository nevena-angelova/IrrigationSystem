import {PlantType} from './plant-type.model';
import {Report} from './report.model';

export interface Plant {
    id: number;
    plantingDate: Date;
    plantType: PlantType;
    deviceId: number;
    relayId: number;
    icon: string;
    report: Report;
}
