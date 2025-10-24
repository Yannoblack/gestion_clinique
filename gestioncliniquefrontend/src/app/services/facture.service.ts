import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService, Facture } from './api.service';

// Ré-export des interfaces pour faciliter l'import
export { Facture } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class FactureService {
  private apiService = inject(ApiService);

  getAllFactures(): Observable<Facture[]> {
    return this.apiService.getFactures();
  }

  getFactureById(id: number): Observable<Facture> {
    return this.apiService.getFactureById(id);
  }

  getFacturesByPatientId(patientId: number): Observable<Facture[]> {
    return this.apiService.getFacturesByPatientId(patientId);
  }

  createFacture(facture: Facture): Observable<Facture> {
    return this.apiService.createFacture(facture);
  }

  updateFacture(id: number, facture: Facture): Observable<Facture> {
    return this.apiService.updateFacture(id, facture);
  }

  deleteFacture(id: number): Observable<void> {
    return this.apiService.deleteFacture(id);
  }

  linkFactureToRendezVous(factureId: number, rendezVousId: number): Observable<Facture> {
    return this.apiService.linkFactureToRendezVous(factureId, rendezVousId);
  }

  // Méthodes utilitaires
  formatAmount(amount: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(amount);
  }

  formatDate(date: string): string {
    const dateObj = new Date(date);
    return dateObj.toLocaleDateString('fr-FR');
  }

  getStatusColor(status: string): string {
    switch (status?.toLowerCase()) {
      case 'payee':
      case 'payé':
        return 'success';
      case 'non_payee':
      case 'non payé':
        return 'warning';
      case 'en_attente':
        return 'info';
      case 'annulee':
      case 'annulé':
        return 'danger';
      default:
        return 'secondary';
    }
  }

  getStatusText(status: string): string {
    switch (status?.toLowerCase()) {
      case 'payee':
      case 'payé':
        return 'Payée';
      case 'non_payee':
      case 'non payé':
        return 'Non payée';
      case 'en_attente':
        return 'En attente';
      case 'annulee':
      case 'annulé':
        return 'Annulée';
      default:
        return status || 'Inconnu';
    }
  }

  calculateTotal(factures: Facture[]): number {
    return factures.reduce((total, facture) => total + facture.montant, 0);
  }

  getUnpaidFactures(factures: Facture[]): Facture[] {
    return factures.filter(facture => 
      facture.statut?.toLowerCase() === 'non_payee' || 
      facture.statut?.toLowerCase() === 'non payé'
    );
  }
}
