import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment.development';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CreateCrop} from '../model/create-crop.model';

@Injectable({
  providedIn: 'root'
})
export class CropService {
  private apiUrl = `${environment.apiUrl}api/crop/`;

  constructor(private httpClient: HttpClient) {
  }

  getCropTypes(): Observable<any> {
    return this.httpClient.get<any>(`${this.apiUrl}types`);
  }

  createCrop(crop: CreateCrop): Observable<String> {
    return this.httpClient.post<any>(`${this.apiUrl}create`, crop, { responseType: 'text' as 'json' });
  }

  getCrops(): Observable<any>  {
    return this.httpClient.get<any>(`${this.apiUrl}crops`);
  }
}
