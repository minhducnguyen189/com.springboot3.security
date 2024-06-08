import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  onLogin() {
    window.location.href = "http://localhost:4200/v1/authentication/login";
  }

}
