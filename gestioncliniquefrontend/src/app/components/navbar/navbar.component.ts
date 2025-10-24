import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav style="background-color: #343a40; color: white; padding: 1rem 0;">
      <div style="max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center; padding: 0 16px;">
        <div style="display: flex; align-items: center;">
          <h1 style="margin: 0; color: white; font-size: 1.5rem;">
            <a routerLink="/patients" style="text-decoration: none; color: white;">Gestion Clinique</a>
          </h1>
        </div>
        
        <div *ngIf="authService.isAuthenticated()" style="display: flex; align-items: center; gap: 1rem;">
          <span style="color: #adb5bd;">Bienvenue, {{ authService.getUsername() }}</span>
          <div style="display: flex; gap: 0.5rem;">
            <!-- Menu pour les administrateurs -->
            <ng-container *ngIf="authService.isAdmin()">
              <a routerLink="/patients" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Patients
              </a>
              <a routerLink="/medecins" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Médecins
              </a>
              <a routerLink="/rendez-vous" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Rendez-vous
              </a>
              <a routerLink="/factures" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Factures
              </a>
              <a routerLink="/prescriptions" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Prescriptions
              </a>
            </ng-container>
            
            <!-- Menu pour les médecins -->
            <ng-container *ngIf="authService.isMedecin()">
              <a routerLink="/patients" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Mes Patients
              </a>
              <a routerLink="/rendez-vous" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Mes Rendez-vous
              </a>
              <a routerLink="/prescriptions" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Mes Prescriptions
              </a>
            </ng-container>
            
            <!-- Menu pour les patients -->
            <ng-container *ngIf="authService.isPatient()">
              <a routerLink="/rendez-vous" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Mes Rendez-vous
              </a>
              <a routerLink="/prescriptions" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Mes Prescriptions
              </a>
              <a routerLink="/factures" style="color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; transition: background-color 0.2s;" 
                 onmouseover="this.style.backgroundColor='#495057'" 
                 onmouseout="this.style.backgroundColor='transparent'">
                Mes Factures
              </a>
            </ng-container>
            
            <button (click)="logout()" style="background-color: #dc3545; color: white; border: none; padding: 0.5rem 1rem; border-radius: 4px; cursor: pointer; margin-left: 0.5rem;">
              Déconnexion
            </button>
          </div>
        </div>
      </div>
    </nav>
  `
})
export class NavbarComponent {
  authService = inject(AuthService);
  router = inject(Router);

  logout() {
    this.authService.logout();
  }
}
