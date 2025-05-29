export interface Report {
    cropId: number;
    needsIrrigation: boolean;
    warnings: string[];
    humidity: number;
    minHumidity: number;
    maxHumidity: number;
    growthPhase: string;
}
