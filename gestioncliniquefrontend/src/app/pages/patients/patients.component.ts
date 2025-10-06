import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, Patient } from '../../services/api.service';

@Component({
  selector: 'app-patients',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="max-width:800px;margin:24px auto;">
      <h2>Patients</h2>
      <div *ngIf="loading">Chargement...</div>
      <div *ngIf="error" style="color:#b00020;">{{ error }}</div>
      <table *ngIf="!loading && patients.length" border="1" cellpadding="6" cellspacing="0">
        <thead><tr><th>Nom</th><th>Prénom</th><th>Téléphone</th><th>Email</th></tr></thead>
        <tbody>
          <tr *ngFor="let p of patients">
            <td>{{ p.nom }}</td>
            <td>{{ p.prenom }}</td>
            <td>{{ p.telephone || '-' }}</td>
            <td>{{ p.email || '-' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  `
})
export class PatientsComponent {
  private readonly api = inject(ApiService);
  patients: Patient[] = [];
  loading = true;
  error = '';

  constructor() {
    this.api.getPatients().subscribe({
      next: data => { this.patients = data; this.loading = false; },
      error: () => { this.error = 'Erreur de chargement'; this.loading = false; }
    });
  }
}


