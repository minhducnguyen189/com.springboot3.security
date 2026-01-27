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
import { MatDialogModule } from '@angular/material/dialog';
import { StartupService } from './service/startup.service';
import { ErrorPageComponent } from './error-page/error-page.component';
import { HttpClientModule } from '@angular/common/http';
import { ApiModule, Configuration } from './rest-client';
import { AuthInterceptor } from './interceptor/auth-interceptor';
import { TransactionsComponent } from './dashboard/transactions/transactions.component';
import { AccountsComponent } from './dashboard/accounts/accounts.component';
import { SharedComponentsModule } from "shared-components";
import { RefreshTokenDialogComponent } from './refresh-token-dialog/refresh-token-dialog.component';
import { RefreshActionDialogComponent } from './refresh-token-dialog/refresh-action-dialog/refresh-action-dialog.component';
import { TokenRefreshSuccessComponent } from './token-refresh-success/token-refresh-success.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ErrorPageComponent,
    TransactionsComponent,
    AccountsComponent,
    RefreshTokenDialogComponent,
    RefreshActionDialogComponent,
    TokenRefreshSuccessComponent
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
    MatDialogModule,
    HttpClientModule,
    ApiModule.forRoot(() => new Configuration({
        basePath: window.location.origin + '/api'
    })),
    SharedComponentsModule
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
