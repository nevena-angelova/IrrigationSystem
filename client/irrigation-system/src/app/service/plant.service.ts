import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment.development';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CreatePlant} from '../model/create-plant.model';

@Injectable({
    providedIn: 'root'
})
export class PlantService {
    private apiUrl = `${environment.apiUrl}api/plant/`;

    constructor(private httpClient: HttpClient) {
    }

    getPlantTypes(): Observable<any> {
        return this.httpClient.get<any>(`${this.apiUrl}types`);
    }

    createPlant(plant: CreatePlant): Observable<String> {
        return this.httpClient.post<any>(`${this.apiUrl}create`, plant, {responseType: 'text' as 'json'});
    }

    getPlants(): Observable<any> {
        return this.httpClient.get<any>(`${this.apiUrl}plants`);
    }

    irrigate(deviceId: number, relayId: number, irrigationDuration: number): Observable<String> {
        return this.httpClient.post<any>(`${this.apiUrl}irrigate/${deviceId}/${relayId}/${irrigationDuration}`, {}, {responseType: 'text' as 'json'});
    }
}
