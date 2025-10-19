import {Routes} from '@angular/router';
import {authGuard} from './service/auth.guard';
import {PlantComponent} from './component/plant/plant.component';
import {AuthenticationComponent} from './component/authentication/authentication.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'Plant',
    pathMatch: 'full'
  },
  {
    path: 'authentication',
    component: AuthenticationComponent
  },
  {
    path: 'Plant',
    component: PlantComponent,
    canActivate: [authGuard]
  }
];
