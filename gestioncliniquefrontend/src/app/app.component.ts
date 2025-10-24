import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, CommonModule],
  template: `
    <div style="min-height: 100vh; background-color: #f8f9fa;">
      <app-navbar *ngIf="authService.isAuthenticated()"></app-navbar>
      <main style="padding-top: 0;">
        <router-outlet></router-outlet>
      </main>
    </div>
  `
})
export class AppComponent {
  authService = inject(AuthService);
  title = 'gestioncliniquefrontend';
}
