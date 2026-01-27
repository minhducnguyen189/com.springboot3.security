import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { authGuard } from './guards/auth.guard';
import { loginGuard } from './guards/login.guard';
import { ErrorPageComponent } from './error-page/error-page.component';
import { TransactionsComponent } from './dashboard/transactions/transactions.component';
import { AccountsComponent } from './dashboard/accounts/accounts.component';
import { TokenRefreshSuccessComponent } from './token-refresh-success/token-refresh-success.component';

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'login'},
  {path: 'login', pathMatch: 'full', canActivate: [loginGuard],  component: LoginComponent},
  {
    path: 'dashboard',
    canActivate: [authGuard],
    component: DashboardComponent,
    children: [
      { path: '', redirectTo: 'transactions', pathMatch: 'full' },
      { path: 'transactions', component: TransactionsComponent },
      { path: 'accounts', component: AccountsComponent }
    ]
  },
  {path: 'token-refresh/success', component: TokenRefreshSuccessComponent},
  {path: 'error', pathMatch: 'full', component:  ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
