import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RecaptchaV3Module, RECAPTCHA_V3_SITE_KEY } from 'ng-recaptcha';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { environment } from '../environments/environment.development';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RecaptchaV3Module,
    FormsModule,
    HttpClientModule,
  ],
  providers: [
    {
      provide: RECAPTCHA_V3_SITE_KEY,
      useValue: environment.recaptcha.siteKey,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
