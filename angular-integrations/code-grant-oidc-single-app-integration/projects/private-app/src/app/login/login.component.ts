import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  errorUrl!: string;

  constructor(private _router: ActivatedRoute) {
   const errorPath = this._router.snapshot.queryParamMap.get('errorPath');
   if (errorPath) {
     this.errorUrl = errorPath;
   }
  }

  onLogin() {
    console.log(this.errorUrl);
    if(this.errorUrl) {
      window.location.href = window.location.origin +
      "/api/private-app/actions/authenticate?redirectPath=/pages/private-app" +
      this.errorUrl;
    } else {
      window.location.href = window.location.origin + "/api/private-app/actions/authenticate?redirectPath=/pages/private-app/dashboard";
    }
  }

}
