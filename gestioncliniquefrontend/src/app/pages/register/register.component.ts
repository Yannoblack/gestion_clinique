import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div style="max-width: 400px; margin: 40px auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
      <form [formGroup]="form" (ngSubmit)="submit()" style="display: flex; flex-direction: column; gap: 16px;">
        <h2 style="text-align: center; margin-bottom: 20px;">Inscription</h2>
        <div>
          <label for="username">Nom d'utilisateur:</label>
          <input 
            id="username"
            placeholder="Nom d'utilisateur" 
            formControlName="username" 
            style="width: 100%; padding: 8px; margin-top: 4px; border: 1px solid #ccc; border-radius: 4px;"
          />
          <div *ngIf="form.get('username')?.invalid && form.get('username')?.touched" style="color: #dc3545; font-size: 12px;">
            Le nom d'utilisateur est requis
          </div>
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
          <div *ngIf="form.get('password')?.invalid && form.get('password')?.touched" style="color: #dc3545; font-size: 12px;">
            Le mot de passe est requis (minimum 6 caractères)
          </div>
        </div>
        <div>
          <label for="confirmPassword">Confirmer le mot de passe:</label>
          <input 
            id="confirmPassword"
            placeholder="Confirmer le mot de passe" 
            type="password" 
            formControlName="confirmPassword"
            style="width: 100%; padding: 8px; margin-top: 4px; border: 1px solid #ccc; border-radius: 4px;"
          />
          <div *ngIf="form.get('confirmPassword')?.invalid && form.get('confirmPassword')?.touched" style="color: #dc3545; font-size: 12px;">
            <div *ngIf="form.get('confirmPassword')?.errors?.['required']">La confirmation du mot de passe est requise</div>
            <div *ngIf="form.get('confirmPassword')?.errors?.['passwordMismatch']">Les mots de passe ne correspondent pas</div>
          </div>
        </div>
        <div>
          <label for="role">Rôle:</label>
          <select 
            id="role"
            formControlName="role"
            style="width: 100%; padding: 8px; margin-top: 4px; border: 1px solid #ccc; border-radius: 4px;"
          >
            <option value="ROLE_PATIENT">Patient</option>
          </select>
          <div style="color: #6c757d; font-size: 12px; margin-top: 4px;">
            Seuls les patients peuvent s'inscrire via cette interface
          </div>
        </div>
        <button 
          type="submit" 
          [disabled]="form.invalid || loading"
          style="padding: 12px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
        >
          {{ loading ? 'Inscription...' : 'S\'inscrire' }}
        </button>
        
        <button 
          type="button" 
          (click)="testConnection()"
          style="padding: 12px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-left: 10px;"
        >
          Tester la connexion
        </button>
        
        <!-- Debug info -->
        <div *ngIf="form.invalid" style="background-color: #f8f9fa; padding: 10px; border-radius: 4px; font-size: 12px;">
          <strong>Debug - Form Status:</strong><br>
          Valid: {{ form.valid }}<br>
          Username errors: {{ form.get('username')?.errors | json }}<br>
          Password errors: {{ form.get('password')?.errors | json }}<br>
          Confirm password errors: {{ form.get('confirmPassword')?.errors | json }}<br>
          Form errors: {{ form.errors | json }}
        </div>
        <div *ngIf="error" style="color: #dc3545; text-align: center; padding: 8px; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px;">
          {{ error }}
        </div>
        <div style="text-align: center; margin-top: 10px;">
          <a routerLink="/login" style="color: #007bff; text-decoration: none;">
            Déjà un compte ? Se connecter
          </a>
        </div>
      </form>
    </div>
  `
})
export class RegisterComponent {
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  loading = false;
  error = '';

  form = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', Validators.required],
    role: ['ROLE_PATIENT', Validators.required]
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(form: any) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
    } else if (confirmPassword && confirmPassword.errors) {
      delete confirmPassword.errors['passwordMismatch'];
      if (Object.keys(confirmPassword.errors).length === 0) {
        confirmPassword.setErrors(null);
      }
    }
    
    return null;
  }

  submit() {
    console.log('Form submitted', this.form.value);
    console.log('Form valid:', this.form.valid);
    console.log('Form errors:', this.form.errors);
    
    if (this.form.invalid) {
      console.log('Form is invalid, not submitting');
      this.error = 'Veuillez corriger les erreurs dans le formulaire';
      return;
    }
    
    this.loading = true;
    this.error = '';
    
    const { username, password, role } = this.form.getRawValue();
    console.log('Submitting registration:', { username, role });
    
    this.authService.register(username!, password!, role!).subscribe({
      next: (response) => {
        console.log('Registration successful:', response);
        this.router.navigateByUrl('/patients');
      },
      error: (error) => {
        console.error('Registration error:', error);
        this.error = error.error?.message || error.message || 'Erreur lors de l\'inscription';
        this.loading = false;
      }
    });
  }

  testConnection() {
    console.log('Testing backend connection...');
    fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: 'testuser',
        password: 'testpass',
        role: 'ROLE_PATIENT'
      })
    })
    .then(response => {
      console.log('Backend response status:', response.status);
      return response.json();
    })
    .then(data => {
      console.log('Backend response data:', data);
      this.error = 'Connexion au backend réussie ! Vérifiez la console pour les détails.';
    })
    .catch(error => {
      console.error('Backend connection error:', error);
      this.error = 'Erreur de connexion au backend: ' + error.message;
    });
  }
}
