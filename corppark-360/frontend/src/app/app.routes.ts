import { Routes } from '@angular/router';
import { LoginComponent } from './components/login.component';
import { DashboardComponent } from './components/dashboard.component';
import { LotViewComponent } from './components/lot-view.component';
import { VisitorInviteComponent } from './components/visitor-invite.component';
import { AdminComponent } from './components/admin.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'lot', component: LotViewComponent },
  { path: 'visitor', component: VisitorInviteComponent },
  { path: 'admin', component: AdminComponent }
];
