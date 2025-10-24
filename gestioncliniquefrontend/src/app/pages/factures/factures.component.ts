import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FactureService, Facture } from '../../services/facture.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-factures',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="max-width: 1200px; margin: 24px auto; padding: 0 16px;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <h2>{{ authService.isPatient() ? 'Mes Factures' : 'Gestion des Factures' }}</h2>
        <div *ngIf="!authService.isPatient()">
          <button (click)="createFacture()" style="padding: 8px 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">
            Nouvelle Facture
          </button>
        </div>
      </div>
      
      <div *ngIf="loading" style="text-align: center; padding: 40px;">
        <div>Chargement des factures...</div>
      </div>
      
      <div *ngIf="error" style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px; padding: 12px; margin-bottom: 16px;">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error" style="background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
        <div style="padding: 16px; background-color: #f8f9fa; border-bottom: 1px solid #dee2e6;">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0;">Liste des Factures ({{ factures.length }})</h3>
            <div style="display: flex; gap: 16px; align-items: center;">
              <div style="font-weight: bold; color: #28a745;">
                Total: {{ formatAmount(getTotalAmount()) }}
              </div>
              <div style="font-weight: bold; color: #dc3545;">
                Impayées: {{ getUnpaidCount() }}
              </div>
            </div>
          </div>
        </div>
        
        <div *ngIf="factures.length === 0" style="padding: 40px; text-align: center; color: #6c757d;">
          Aucune facture trouvée
        </div>
        
        <div *ngIf="factures.length > 0" style="overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse;">
            <thead style="background-color: #f8f9fa;">
              <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">N° Facture</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Date</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Montant</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Statut</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Rendez-vous</th>
                <th style="padding: 12px; text-align: center; border-bottom: 2px solid #dee2e6;">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let facture of factures" style="border-bottom: 1px solid #dee2e6;">
                <td style="padding: 12px;">#{{ facture.id }}</td>
                <td style="padding: 12px;">{{ formatDate(facture.dateFacture) }}</td>
                <td style="padding: 12px; font-weight: bold;">{{ formatAmount(facture.montant) }}</td>
                <td style="padding: 12px;">
                  <span [style]="'color: ' + getStatusColor(facture.statut || '') + '; font-weight: bold;'">
                    {{ getStatusText(facture.statut || '') }}
                  </span>
                </td>
                <td style="padding: 12px;">{{ facture.rendezVousId ? 'RV #' + facture.rendezVousId : '-' }}</td>
                <td style="padding: 12px; text-align: center;">
                  <button (click)="viewFacture(facture)" style="padding: 4px 8px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Voir
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="editFacture(facture)" style="padding: 4px 8px; background-color: #ffc107; color: black; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Modifier
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="deleteFacture(facture)" style="padding: 4px 8px; background-color: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Supprimer
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal de création/édition de facture -->
    <div *ngIf="showCreateModal" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000; display: flex; justify-content: center; align-items: center;">
      <div style="background-color: white; padding: 30px; border-radius: 8px; width: 90%; max-width: 500px;">
        <h3 style="margin-top: 0; margin-bottom: 20px;">{{ editingFacture ? 'Modifier la facture' : 'Nouvelle facture' }}</h3>
        
        <form style="display: flex; flex-direction: column; gap: 15px;">
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Montant *</label>
              <input 
                [(ngModel)]="factureForm.montant" 
                type="number"
                step="0.01"
                placeholder="0.00"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Date *</label>
              <input 
                [(ngModel)]="factureForm.dateFacture" 
                type="date"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Statut</label>
              <select 
                [(ngModel)]="factureForm.statut" 
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              >
                <option value="NON_PAYEE">Non payée</option>
                <option value="PAYEE">Payée</option>
                <option value="EN_ATTENTE">En attente</option>
                <option value="ANNULEE">Annulée</option>
              </select>
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Rendez-vous ID</label>
              <input 
                [(ngModel)]="factureForm.rendezVousId" 
                type="number"
                placeholder="ID du rendez-vous (optionnel)"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
          </div>

          <div *ngIf="error" style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px; padding: 8px;">
            {{ error }}
          </div>

          <div style="display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px;">
            <button 
              type="button" 
              (click)="closeModal()" 
              style="padding: 10px 20px; background-color: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer;"
            >
              Annuler
            </button>
            <button 
              type="button" 
              (click)="saveFacture()" 
              [disabled]="loading || !factureForm.montant || !factureForm.dateFacture"
              style="padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
            >
              {{ loading ? 'Enregistrement...' : (editingFacture ? 'Modifier' : 'Créer') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class FacturesComponent implements OnInit {
  private readonly factureService = inject(FactureService);
  readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  
  factures: Facture[] = [];
  loading = true;
  error = '';
  showCreateModal = false;
  editingFacture: Facture | null = null;
  factureForm = {
    montant: 0,
    dateFacture: '',
    statut: 'NON_PAYEE',
    rendezVousId: 0
  };

  ngOnInit() {
    this.loadFactures();
  }

  loadFactures() {
    this.loading = true;
    this.error = '';
    
    this.factureService.getAllFactures().subscribe({
      next: (data) => {
        this.factures = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des factures';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  formatAmount(amount: number): string {
    return this.factureService.formatAmount(amount);
  }

  formatDate(date: string): string {
    return this.factureService.formatDate(date);
  }

  getStatusColor(status: string): string {
    return this.factureService.getStatusColor(status);
  }

  getStatusText(status: string): string {
    return this.factureService.getStatusText(status);
  }

  getTotalAmount(): number {
    return this.factureService.calculateTotal(this.factures);
  }

  getUnpaidCount(): number {
    return this.factureService.getUnpaidFactures(this.factures).length;
  }

  createFacture() {
    this.showCreateModal = true;
    this.editingFacture = null;
    this.resetForm();
  }

  viewFacture(facture: Facture) {
    // TODO: Implémenter la vue détaillée de la facture
    console.log('Voir la facture:', facture);
  }

  editFacture(facture: Facture) {
    // TODO: Implémenter l'édition de la facture
    console.log('Modifier la facture:', facture);
  }

  deleteFacture(facture: Facture) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la facture #${facture.id} ?`)) {
      if (facture.id) {
        this.factureService.deleteFacture(facture.id).subscribe({
          next: () => {
            this.loadFactures(); // Recharger la liste
          },
          error: (error) => {
            this.error = 'Erreur lors de la suppression de la facture';
            console.error('Erreur:', error);
          }
        });
      }
    }
  }

  resetForm() {
    this.factureForm = {
      montant: 0,
      dateFacture: '',
      statut: 'NON_PAYEE',
      rendezVousId: 0
    };
  }

  saveFacture() {
    if (!this.factureForm.montant || !this.factureForm.dateFacture) {
      this.error = 'Le montant et la date sont obligatoires';
      return;
    }

    const factureData: Facture = {
      montant: this.factureForm.montant,
      dateFacture: this.factureForm.dateFacture,
      statut: this.factureForm.statut,
      rendezVousId: this.factureForm.rendezVousId || undefined
    };

    this.loading = true;
    this.error = '';

    this.factureService.createFacture(factureData).subscribe({
      next: (newFacture) => {
        this.loadFactures(); // Recharger la liste
        this.showCreateModal = false;
        this.resetForm();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors de la création de la facture';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  closeModal() {
    this.showCreateModal = false;
    this.editingFacture = null;
    this.resetForm();
  }
}
