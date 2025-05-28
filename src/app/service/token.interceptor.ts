import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';

export const tokenInterceptorFn: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwt');
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        alert('Your session has expired. Please log in again.');
        localStorage.removeItem('jwt');
        window.location.href = '/authentication';
      }
      return throwError(() => error);
    })
  );
};
