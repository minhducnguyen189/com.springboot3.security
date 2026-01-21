import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withXsrfConfiguration } from '@angular/common/http';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { StartupService } from './service/startup.service';
import { ErrorPageComponent } from './error-page/error-page.component';
import { HttpClientModule } from '@angular/common/http';
import { ApiModule, Configuration } from './rest-client';
import { AuthInterceptor } from './interceptor/auth-interceptor';
import { SharedComponentsModule } from 'shared-components';
import { TransactionsComponent } from './dashboard/transactions/transactions.component';
import { AccountsComponent } from './dashboard/accounts/accounts.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ErrorPageComponent,
    TransactionsComponent,
    AccountsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    MatGridListModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatSlideToggleModule,
    SharedComponentsModule,
    HttpClientModule,
    ApiModule.forRoot(() => new Configuration({
      basePath: window.location.origin + '/api'
    }))
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: (startupService: StartupService) => (): void => startupService.initApplication(),
      deps: [StartupService],
      multi: true
    },
     provideHttpClient(
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN', // Default name in the cookie
        headerName: 'X-XSRF-TOKEN' // Default name in the header
      })
    ),
    {
      provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true
    }
  
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
