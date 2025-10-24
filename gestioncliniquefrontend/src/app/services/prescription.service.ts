import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService, Prescription } from './api.service';

// Ré-export des interfaces pour faciliter l'import
export { Prescription } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class PrescriptionService {
  private apiService = inject(ApiService);

  getAllPrescriptions(): Observable<Prescription[]> {
    return this.apiService.getPrescriptions();
  }

  getPrescriptionById(id: number): Observable<Prescription> {
    return this.apiService.getPrescriptionById(id);
  }

  getPrescriptionsByRendezVous(rendezVousId: number): Observable<Prescription[]> {
    return this.apiService.getPrescriptionsByRendezVous(rendezVousId);
  }

  getPrescriptionsByMedecin(medecinId: number): Observable<Prescription[]> {
    return this.apiService.getPrescriptionsByMedecin(medecinId);
  }

  createPrescription(prescription: Prescription): Observable<Prescription> {
    return this.apiService.createPrescription(prescription);
  }

  updatePrescription(id: number, prescription: Prescription): Observable<Prescription> {
    return this.apiService.updatePrescription(id, prescription);
  }

  deletePrescription(id: number): Observable<void> {
    return this.apiService.deletePrescription(id);
  }

  // Méthodes utilitaires
  formatMedicaments(medicaments: string): string[] {
    if (!medicaments) return [];
    return medicaments.split(',').map(med => med.trim()).filter(med => med.length > 0);
  }

  formatInstructions(instructions: string): string[] {
    if (!instructions) return [];
    return instructions.split('\n').map(inst => inst.trim()).filter(inst => inst.length > 0);
  }

  validatePrescription(prescription: Prescription): string[] {
    const errors: string[] = [];
    
    if (!prescription.idPatient) {
      errors.push('L\'ID du patient est requis');
    }
    
    if (!prescription.idMedecin) {
      errors.push('L\'ID du médecin est requis');
    }
    
    if (!prescription.medicaments || prescription.medicaments.trim().length === 0) {
      errors.push('Les médicaments sont requis');
    }
    
    if (!prescription.instructions || prescription.instructions.trim().length === 0) {
      errors.push('Les instructions sont requises');
    }
    
    return errors;
  }

  createPrescriptionFromRendezVous(rendezVousId: number, patientId: number, medecinId: number): Prescription {
    return {
      idRendezVous: rendezVousId,
      idPatient: patientId,
      idMedecin: medecinId,
      medicaments: '',
      instructions: '',
      autresInformations: ''
    };
  }
}
