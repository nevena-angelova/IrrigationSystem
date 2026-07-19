import {Routes} from '@angular/router';
import {authGuard} from './service/auth.guard';
import {PlantComponent} from './component/plant/plant.component';
import {AuthenticationComponent} from './component/authentication/authentication.component';
import {StatisticComponent} from './component/statistic/statistic.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'plant',
    pathMatch: 'full'
  },
  {
    path: 'authentication',
    component: AuthenticationComponent
  },
  {
    path: 'plant',
    component: PlantComponent,
    canActivate: [authGuard]
  },
  {
    path: 'statistic',
    component: StatisticComponent,
    canActivate: [authGuard]
  }
];
