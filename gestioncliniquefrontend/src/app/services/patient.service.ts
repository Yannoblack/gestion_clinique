import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService, Patient } from './api.service';

// Ré-export des interfaces pour faciliter l'import
export { Patient } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private apiService = inject(ApiService);

  getAllPatients(): Observable<Patient[]> {
    return this.apiService.getPatients();
  }

  getPatientById(id: number): Observable<Patient> {
    return this.apiService.getPatientById(id);
  }

  searchPatients(query: string): Observable<Patient[]> {
    return this.apiService.searchPatients(query);
  }

  createPatient(patient: Patient): Observable<Patient> {
    return this.apiService.createPatient(patient);
  }

  updatePatient(id: number, patient: Patient): Observable<Patient> {
    return this.apiService.updatePatient(id, patient);
  }

  deletePatient(id: number): Observable<void> {
    return this.apiService.deletePatient(id);
  }

  // Méthodes utilitaires
  formatPatientName(patient: Patient): string {
    return `${patient.prenom} ${patient.nom}`;
  }

  formatPatientInfo(patient: Patient): string {
    let info = this.formatPatientName(patient);
    if (patient.telephone) {
      info += ` - ${patient.telephone}`;
    }
    if (patient.email) {
      info += ` - ${patient.email}`;
    }
    return info;
  }
}
