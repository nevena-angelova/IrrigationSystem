import {Component} from '@angular/core';
import {LoginComponent} from "./login/login.component";
import {Router} from "@angular/router";
import {User} from '../../model/user.model';
import {AuthenticationService} from '../../service/authentication.service';
import {NgIf} from '@angular/common';
import {RegisterComponent} from './register/register.component';

@Component({
  selector: 'app-authentication',
    imports: [
        LoginComponent,
        NgIf,
        RegisterComponent
    ],
  templateUrl: './authentication.component.html',
  styleUrl: './authentication.component.css'
})
export class AuthenticationComponent {
  login = true;

  constructor(private authenticationService: AuthenticationService, private router: Router) {}

  authFormSubmit(user: User) {
    this.authenticationService.authenticate(user, this.login ? 'login' : 'register' ).subscribe({
      next: (data) => this.router.navigate(['/Plant']),
      error: (err) => console.error('Error loading data', err)
    });
  }

  onToggleForm() {
    this.login = !this.login;
  }
}
