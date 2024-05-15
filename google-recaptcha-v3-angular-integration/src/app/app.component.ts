import { Component } from '@angular/core';
import { FormControl, NgForm } from '@angular/forms';
import { ReCaptchaV3Service } from 'ng-recaptcha';
import { GoogleRecaptchaService } from './service/google-recaptcha.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private recaptchaV3Service: ReCaptchaV3Service,
    private googleRecaptchaService: GoogleRecaptchaService) {
  }

  public send(form: NgForm): void {
    if (form.invalid) {
      for (const control of Object.keys(form.controls)) {
        form.controls[control].markAsTouched();
      }
      return;
    }

    this.recaptchaV3Service.execute('importantAction')
    .subscribe((token: string) => {
      console.debug(`Token [${token}] generated`);
      this.googleRecaptchaService.verifyRecaptchaToken(token)
      .subscribe(result => console.log(result));
    });
  }

}
