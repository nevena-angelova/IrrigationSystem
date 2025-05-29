import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment.development';
import {Observable, tap} from 'rxjs';
import {User} from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private apiUrl = `${environment.apiUrl}api/auth/`;

  constructor(private httpClient: HttpClient) {
  }

  authenticate(user: User, method: String): Observable<any> {
    return this.httpClient.post<any>(`${this.apiUrl}${method}`, user)
      .pipe(
        tap(response => {
          if (response.token) {
            localStorage.setItem('jwt', response.token);
          }
        }));
  }
}
