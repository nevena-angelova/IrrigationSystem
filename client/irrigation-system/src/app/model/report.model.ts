export class Report {
    constructor(
        public plantId: number = 0,
        public needsIrrigation: boolean = false,
        public warnings: string[] = [],
        public humidity: number = 0,
        public maxHumidity: number = 0,
        public minHumidity: number = 0,
        public growthPhaseName: string = '',
        public growthPhaseDetails: string = '',
        public irrigationDuration: number = 0
    ) {}
}
