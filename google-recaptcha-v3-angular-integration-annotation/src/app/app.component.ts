import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ReCaptchaV3Service } from 'ng-recaptcha';
import { SecretMessage } from './service/secret-message.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private recaptchaV3Service: ReCaptchaV3Service,
    private secretMessage: SecretMessage,
  ) {};

  // formControl: FormControl = new FormControl('');
  secureMessage: string = '';
  
  public getSecureMessage(): void {

    this.recaptchaV3Service.execute('importantAction')
    .subscribe((token: string) => {
      console.debug(`Token [${token}] generated`);
      this.secretMessage.getSecretMessage(token)
      .subscribe(result => 
        this.secureMessage = result
      );
    });
  }

  
}
