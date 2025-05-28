import {Routes} from '@angular/router';
import {LoginComponent} from './component/authentication/login/login.component';
import {RegisterComponent} from './component/authentication/register/register.component';
import {authGuard} from './service/auth.guard';
import {CropComponent} from './component/crop/crop.component';
import {AuthenticationComponent} from './component/authentication/authentication.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'crop',
    pathMatch: 'full'
  },
  {
    path: 'authentication',
    component: AuthenticationComponent
  },
  {
    path: 'crop',
    component: CropComponent,
    canActivate: [authGuard]
  }
];
