import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

export interface Patient {
  id?: number;
  nom: string;
  prenom: string;
  telephone?: string;
  email?: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  login(username: string, password: string) {
    return this.http.post<{ token: string }>(`${this.baseUrl}/auth/login`, { username, password });
  }

  getPatients() {
    return this.http.get<Patient[]>(`${this.baseUrl}/patients`);
  }
}


