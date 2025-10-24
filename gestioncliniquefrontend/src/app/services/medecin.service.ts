import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService, Medecin } from './api.service';

// Ré-export des interfaces pour faciliter l'import
export { Medecin } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class MedecinService {
  private apiService = inject(ApiService);

  getAllMedecins(): Observable<Medecin[]> {
    return this.apiService.getMedecins();
  }

  getMedecinById(id: number): Observable<Medecin> {
    return this.apiService.getMedecinById(id);
  }

  createMedecin(medecin: Medecin): Observable<Medecin> {
    return this.apiService.createMedecin(medecin);
  }

  updateMedecin(id: number, medecin: Medecin): Observable<Medecin> {
    return this.apiService.updateMedecin(id, medecin);
  }

  deleteMedecin(id: number): Observable<void> {
    return this.apiService.deleteMedecin(id);
  }

  // Méthodes utilitaires
  formatMedecinName(medecin: Medecin): string {
    return `Dr. ${medecin.prenom} ${medecin.nom}`;
  }

  formatMedecinInfo(medecin: Medecin): string {
    let info = this.formatMedecinName(medecin);
    if (medecin.specialite) {
      info += ` - ${medecin.specialite}`;
    }
    if (medecin.telephone) {
      info += ` - ${medecin.telephone}`;
    }
    return info;
  }

  getMedecinsBySpecialite(medecins: Medecin[], specialite: string): Medecin[] {
    return medecins.filter(medecin => 
      medecin.specialite.toLowerCase().includes(specialite.toLowerCase())
    );
  }
}
