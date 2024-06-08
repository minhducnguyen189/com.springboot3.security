import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  {path: '', redirectTo: '/pages/dashboard', pathMatch: 'full'},
  {path: 'pages/dashboard', component: DashboardComponent, pathMatch: 'full'},
  {path: 'pages/login', component: LoginComponent,  pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
