import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()" style="max-width:320px;margin:40px auto;display:flex;flex-direction:column;gap:12px;">
      <h2>Connexion</h2>
      <input placeholder="Utilisateur" formControlName="username" />
      <input placeholder="Mot de passe" type="password" formControlName="password" />
      <button type="submit" [disabled]="form.invalid || loading">Se connecter</button>
      <div *ngIf="error" style="color:#b00020;">{{ error }}</div>
    </form>
  `
})
export class LoginComponent {
  private readonly api = inject(ApiService);
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
    const { username, password } = this.form.getRawValue();
    this.api.login(username!, password!).subscribe({
      next: ({ token }) => {
        localStorage.setItem('jwt', token);
        this.router.navigateByUrl('/patients');
      },
      error: () => {
        this.error = 'Identifiants invalides';
        this.loading = false;
      }
    });
  }
}


