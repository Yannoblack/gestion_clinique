import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService, RendezVous } from './api.service';

// Ré-export des interfaces pour faciliter l'import
export { RendezVous } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class RendezVousService {
  private apiService = inject(ApiService);

  getAllRendezVous(): Observable<RendezVous[]> {
    return this.apiService.getRendezVous();
  }

  getRendezVousById(id: number): Observable<RendezVous> {
    return this.apiService.getRendezVousById(id);
  }

  getRendezVousByPatientId(patientId: number): Observable<RendezVous[]> {
    return this.apiService.getRendezVousByPatientId(patientId);
  }

  getRendezVousByMedecinId(medecinId: number): Observable<RendezVous[]> {
    return this.apiService.getRendezVousByMedecinId(medecinId);
  }

  createRendezVous(rendezVous: RendezVous): Observable<RendezVous> {
    return this.apiService.createRendezVous(rendezVous);
  }



  updateRendezVous(id: number, rendezVous: RendezVous): Observable<RendezVous> {
    return this.apiService.updateRendezVous(id, rendezVous);
  }

  deleteRendezVous(id: number): Observable<void> {
    return this.apiService.deleteRendezVous(id);
  }

  checkAvailability(patientId: number, medecinId: number): Observable<any> {
    return this.apiService.checkAvailability(patientId, medecinId);
  }

  // Méthodes utilitaires
  formatDateTime(dateTime: string): string {
    const date = new Date(dateTime);
    return date.toLocaleString('fr-FR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatDate(dateTime: string): string {
    const date = new Date(dateTime);
    return date.toLocaleDateString('fr-FR');
  }

  formatTime(dateTime: string): string {
    const date = new Date(dateTime);
    return date.toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getStatusColor(status: string): string {
    switch (status?.toLowerCase()) {
      case 'planifie':
        return 'primary';
      case 'confirme':
        return 'success';
      case 'annule':
        return 'danger';
      case 'termine':
        return 'info';
      default:
        return 'secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status?.toLowerCase()) {
      case 'planifie':
        return 'Planifié';
      case 'confirme':
        return 'Confirmé';
      case 'annule':
        return 'Annulé';
      case 'termine':
        return 'Terminé';
      default:
        return status || 'Inconnu';
    }
  }
}

