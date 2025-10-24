import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div style="max-width: 400px; margin: 40px auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
      <form [formGroup]="form" (ngSubmit)="submit()" style="display: flex; flex-direction: column; gap: 16px;">
        <h2 style="text-align: center; margin-bottom: 20px;">Connexion</h2>
        <div>
          <label for="username">Nom d'utilisateur:</label>
          <input 
            id="username"
            placeholder="Nom d'utilisateur" 
            formControlName="username" 
            style="width: 100%; padding: 8px; margin-top: 4px; border: 1px solid #ccc; border-radius: 4px;"
          />
        </div>
        <div>
          <label for="password">Mot de passe:</label>
          <input 
            id="password"
            placeholder="Mot de passe" 
            type="password" 
            formControlName="password"
            style="width: 100%; padding: 8px; margin-top: 4px; border: 1px solid #ccc; border-radius: 4px;"
          />
        </div>
        <button 
          type="submit" 
          [disabled]="form.invalid || loading"
          style="padding: 12px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;"
        >
          {{ loading ? 'Connexion...' : 'Se connecter' }}
        </button>
        <div *ngIf="error" style="color: #dc3545; text-align: center; padding: 8px; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px;">
          {{ error }}
        </div>
        <div style="text-align: center; margin-top: 10px;">
          <a routerLink="/register" style="color: #007bff; text-decoration: none;">
            Pas de compte ? S'inscrire
          </a>
        </div>
      </form>
    </div>
  `
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  loading = false;
  error = '';

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    
    const { username, password } = this.form.getRawValue();
    this.authService.login(username!, password!).subscribe({
      next: () => {
        this.router.navigateByUrl('/patients');
      },
      error: (error) => {
        this.error = error.error?.message || 'Identifiants invalides';
        this.loading = false;
      }
    });
  }
}


