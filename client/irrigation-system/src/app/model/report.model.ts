export class Report {
    constructor(
        public plantId: number = 0,
        public warnings: string[] = [],
        public soilMoisture: number = 0,
        public maxSoilMoisture: number = 0,
        public minSoilMoisture: number = 0,
        public temperature: number = 0,
        public humidity: number = 0,
        public light: number = 0,
        public growthPhaseName: string = '',
        public growthPhaseDetails: string = '',
    ) {}
}
