import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PrescriptionService, Prescription } from '../../services/prescription.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-prescriptions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="max-width: 1200px; margin: 24px auto; padding: 0 16px;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <h2>{{ authService.isPatient() ? 'Mes Prescriptions' : 'Gestion des Prescriptions' }}</h2>
        <div *ngIf="!authService.isPatient()">
          <button (click)="createPrescription()" style="padding: 8px 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">
            Nouvelle Prescription
          </button>
        </div>
      </div>
      
      <div *ngIf="loading" style="text-align: center; padding: 40px;">
        <div>Chargement des prescriptions...</div>
      </div>
      
      <div *ngIf="error" style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px; padding: 12px; margin-bottom: 16px;">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error" style="background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
        <div style="padding: 16px; background-color: #f8f9fa; border-bottom: 1px solid #dee2e6;">
          <h3 style="margin: 0;">Liste des Prescriptions ({{ prescriptions.length }})</h3>
        </div>
        
        <div *ngIf="prescriptions.length === 0" style="padding: 40px; text-align: center; color: #6c757d;">
          Aucune prescription trouvée
        </div>
        
        <div *ngIf="prescriptions.length > 0" style="overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse;">
            <thead style="background-color: #f8f9fa;">
              <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">N° Prescription</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Patient ID</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Médecin ID</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Médicaments</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Instructions</th>
                <th style="padding: 12px; text-align: center; border-bottom: 2px solid #dee2e6;">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let prescription of prescriptions" style="border-bottom: 1px solid #dee2e6;">
                <td style="padding: 12px;">#{{ prescription.id }}</td>
                <td style="padding: 12px;">Patient #{{ prescription.idPatient }}</td>
                <td style="padding: 12px;">Médecin #{{ prescription.idMedecin }}</td>
                <td style="padding: 12px; max-width: 200px; word-wrap: break-word;">
                  {{ prescription.medicaments }}
                </td>
                <td style="padding: 12px; max-width: 200px; word-wrap: break-word;">
                  {{ prescription.instructions }}
                </td>
                <td style="padding: 12px; text-align: center;">
                  <button (click)="viewPrescription(prescription)" style="padding: 4px 8px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Voir
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="editPrescription(prescription)" style="padding: 4px 8px; background-color: #ffc107; color: black; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Modifier
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="deletePrescription(prescription)" style="padding: 4px 8px; background-color: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Supprimer
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal de création/édition de prescription -->
    <div *ngIf="showCreateModal" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000; display: flex; justify-content: center; align-items: center;">
      <div style="background-color: white; padding: 30px; border-radius: 8px; width: 90%; max-width: 600px; max-height: 90vh; overflow-y: auto;">
        <h3 style="margin-top: 0; margin-bottom: 20px;">{{ editingPrescription ? 'Modifier la prescription' : 'Nouvelle prescription' }}</h3>
        
        <form style="display: flex; flex-direction: column; gap: 15px;">
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Médicaments *</label>
              <input 
                [(ngModel)]="prescriptionForm.medicaments" 
                placeholder="Nom du médicament"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Posologie *</label>
              <input 
                [(ngModel)]="prescriptionForm.posologie" 
                placeholder="Ex: 1 comprimé matin et soir"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Durée</label>
              <input 
                [(ngModel)]="prescriptionForm.duree" 
                placeholder="Ex: 7 jours, 2 semaines"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Rendez-vous ID</label>
              <input 
                [(ngModel)]="prescriptionForm.rendezVousId" 
                type="number"
                placeholder="ID du rendez-vous (optionnel)"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
          </div>

          <div>
            <label style="display: block; margin-bottom: 5px; font-weight: bold;">Instructions spéciales</label>
            <textarea 
              [(ngModel)]="prescriptionForm.instructions" 
              placeholder="Instructions particulières pour le patient"
              style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; min-height: 80px; resize: vertical;"
            ></textarea>
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
              (click)="savePrescription()" 
              [disabled]="loading || !prescriptionForm.medicaments || !prescriptionForm.posologie"
              style="padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
            >
              {{ loading ? 'Enregistrement...' : (editingPrescription ? 'Modifier' : 'Créer') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class PrescriptionsComponent implements OnInit {
  private readonly prescriptionService = inject(PrescriptionService);
  readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  
  prescriptions: Prescription[] = [];
  loading = true;
  error = '';
  showCreateModal = false;
  editingPrescription: Prescription | null = null;
  prescriptionForm = {
    medicaments: '',
    posologie: '',
    duree: '',
    instructions: '',
    rendezVousId: 0
  };

  ngOnInit() {
    this.loadPrescriptions();
  }

  loadPrescriptions() {
    this.loading = true;
    this.error = '';
    
    this.prescriptionService.getAllPrescriptions().subscribe({
      next: (data) => {
        this.prescriptions = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des prescriptions';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  createPrescription() {
    this.showCreateModal = true;
    this.editingPrescription = null;
    this.resetForm();
  }

  viewPrescription(prescription: Prescription) {
    // TODO: Implémenter la vue détaillée de la prescription
    console.log('Voir la prescription:', prescription);
  }

  editPrescription(prescription: Prescription) {
    // TODO: Implémenter l'édition de la prescription
    console.log('Modifier la prescription:', prescription);
  }

  deletePrescription(prescription: Prescription) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la prescription #${prescription.id} ?`)) {
      if (prescription.id) {
        this.prescriptionService.deletePrescription(prescription.id).subscribe({
          next: () => {
            this.loadPrescriptions(); // Recharger la liste
          },
          error: (error) => {
            this.error = 'Erreur lors de la suppression de la prescription';
            console.error('Erreur:', error);
          }
        });
      }
    }
  }

  resetForm() {
    this.prescriptionForm = {
      medicaments: '',
      posologie: '',
      duree: '',
      instructions: '',
      rendezVousId: 0
    };
  }

  savePrescription() {
    if (!this.prescriptionForm.medicaments || !this.prescriptionForm.posologie) {
      this.error = 'Le médicament et la posologie sont obligatoires';
      return;
    }

    const prescriptionData: Prescription = {
      medicaments: this.prescriptionForm.medicaments,
      instructions: this.prescriptionForm.instructions,
      idPatient: 0, // TODO: Récupérer l'ID du patient depuis le contexte
      idMedecin: 0, // TODO: Récupérer l'ID du médecin depuis le contexte
      idRendezVous: this.prescriptionForm.rendezVousId || undefined
    };

    this.loading = true;
    this.error = '';

    this.prescriptionService.createPrescription(prescriptionData).subscribe({
      next: (newPrescription) => {
        this.loadPrescriptions(); // Recharger la liste
        this.showCreateModal = false;
        this.resetForm();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors de la création de la prescription';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  closeModal() {
    this.showCreateModal = false;
    this.editingPrescription = null;
    this.resetForm();
  }
}
