import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { PatientsComponent } from './pages/patients/patients.component';
import { MedecinsComponent } from './pages/medecins/medecins.component';
import { RendezVousComponent } from './pages/rendez-vous/rendez-vous.component';
import { FacturesComponent } from './pages/factures/factures.component';
import { PrescriptionsComponent } from './pages/prescriptions/prescriptions.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'patients', component: PatientsComponent },
  { path: 'medecins', component: MedecinsComponent },
  { path: 'rendez-vous', component: RendezVousComponent },
  { path: 'factures', component: FacturesComponent },
  { path: 'prescriptions', component: PrescriptionsComponent },
  { path: '**', redirectTo: 'login' }
];
