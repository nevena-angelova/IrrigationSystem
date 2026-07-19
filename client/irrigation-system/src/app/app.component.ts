import {Component} from '@angular/core';
import {RouterOutlet, RouterLink, RouterModule} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'irrigation-system';

  isAuthenticated(): boolean {
    return localStorage.getItem('jwt') !== null;
  }
}
