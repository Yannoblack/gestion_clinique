import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ApiService, AuthResponse } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiService = inject(ApiService);
  private router = inject(Router);
  
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor() {
    // Vérifier l'état d'authentification au démarrage
    this.checkAuthStatus();
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.apiService.login(username, password).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('jwt', response.token);
          localStorage.setItem('username', username);
          if (response.role) {
            localStorage.setItem('userRole', response.role);
          }
          this.isAuthenticatedSubject.next(true);
        }
      }),
      catchError(error => {
        console.error('Erreur de connexion:', error);
        return throwError(() => error);
      })
    );
  }

  register(username: string, password: string, role: string = 'ROLE_PATIENT'): Observable<AuthResponse> {
    return this.apiService.register(username, password, role).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('jwt', response.token);
          localStorage.setItem('username', username);
          localStorage.setItem('userRole', role);
          this.isAuthenticatedSubject.next(true);
        }
      }),
      catchError(error => {
        console.error('Erreur d\'inscription:', error);
        return throwError(() => error);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('jwt');
    localStorage.removeItem('username');
    localStorage.removeItem('userRole');
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  isAuthenticated(): boolean {
    return this.hasValidToken();
  }

  private hasValidToken(): boolean {
    const token = localStorage.getItem('jwt');
    if (!token) {
      return false;
    }

    try {
      // Décoder le token JWT pour vérifier s'il est expiré
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Math.floor(Date.now() / 1000);
      
      if (payload.exp && payload.exp < currentTime) {
        // Token expiré
        localStorage.removeItem('jwt');
        localStorage.removeItem('username');
        return false;
      }
      
      return true;
    } catch (error) {
      // Token invalide
      localStorage.removeItem('jwt');
      localStorage.removeItem('username');
      return false;
    }
  }

  private checkAuthStatus(): void {
    const isAuth = this.hasValidToken();
    this.isAuthenticatedSubject.next(isAuth);
  }

  // Méthodes utilitaires
  getAuthHeaders(): { [key: string]: string } {
    const token = this.getToken();
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  handleAuthError(error: any): void {
    if (error.status === 401 || error.status === 403) {
      this.logout();
    }
  }

  getUserRole(): string | null {
    return localStorage.getItem('userRole');
  }

  isAdmin(): boolean {
    return this.getUserRole() === 'ROLE_ADMIN';
  }

  isMedecin(): boolean {
    return this.getUserRole() === 'ROLE_MEDECIN';
  }

  isPatient(): boolean {
    return this.getUserRole() === 'ROLE_PATIENT';
  }
}
