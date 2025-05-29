import {CropType} from './crop-type.model';
import {Report} from './report.model';

export interface Crop {
    id: number;
    plantingDate: Date;
    cropType: CropType;
    icon: string;
    report: Report;
}
