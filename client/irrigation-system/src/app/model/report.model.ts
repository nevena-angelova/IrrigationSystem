export class Report {
    constructor(
        public plantId: number = 0,
        public needsIrrigation: boolean = false,
        public warnings: string[] = [],
        public soilMoisture: number = 0,
        public maxSoilMoisture: number = 0,
        public minSoilMoisture: number = 0,
        public growthPhaseName: string = '',
        public growthPhaseDetails: string = '',
        public irrigationDuration: number = 0
    ) {}
}
