import {environment} from '../../environments/environment.development';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  private apiUrl = `${environment.apiUrl}api/statistic/`;

  constructor(private httpClient: HttpClient) {
  }

  getPlantTypes(): Observable<any> {
    return this.httpClient.get<any>(`${this.apiUrl}etc`);
  }
}
