import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RendezVousService, RendezVous } from '../../services/rendez-vous.service';
import { PatientService, Patient } from '../../services/patient.service';
import { MedecinService, Medecin } from '../../services/medecin.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-rendez-vous',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="max-width: 1200px; margin: 24px auto; padding: 0 16px;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <h2>{{ authService.isPatient() ? 'Mes Rendez-vous' : 'Gestion des Rendez-vous' }}</h2>
        <div *ngIf="!authService.isPatient()">
          <button (click)="createRendezVous()" style="padding: 8px 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">
            Nouveau Rendez-vous
          </button>
        </div>
      </div>
      
      <div *ngIf="loading" style="text-align: center; padding: 40px;">
        <div>Chargement des rendez-vous...</div>
      </div>
      
      <div *ngIf="error" style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px; padding: 12px; margin-bottom: 16px;">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error" style="background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
        <div style="padding: 16px; background-color: #f8f9fa; border-bottom: 1px solid #dee2e6;">
          <h3 style="margin: 0;">Liste des Rendez-vous ({{ rendezVous.length }})</h3>
        </div>
        
        <div *ngIf="rendezVous.length === 0" style="padding: 40px; text-align: center; color: #6c757d;">
          Aucun rendez-vous trouvé
        </div>
        
        <div *ngIf="rendezVous.length > 0" style="overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse;">
            <thead style="background-color: #f8f9fa;">
              <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Date/Heure</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Patient</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Médecin</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Salle</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Statut</th>
                <th style="padding: 12px; text-align: center; border-bottom: 2px solid #dee2e6;">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let rv of rendezVous" style="border-bottom: 1px solid #dee2e6;">
                <td style="padding: 12px;">{{ formatDateTime(rv.dateHeure) }}</td>
                <td style="padding: 12px;">{{ getPatientName(rv.patientId) }}</td>
                <td style="padding: 12px;">{{ getMedecinName(rv.medecinId) }}</td>
                <td style="padding: 12px;">{{ rv.salle }}</td>
                <td style="padding: 12px;">
                  <span [style]="'color: ' + getStatusColor(rv.statut || '') + '; font-weight: bold;'">
                    {{ getStatusText(rv.statut || '') }}
                  </span>
                </td>
                <td style="padding: 12px; text-align: center;">
                  <button (click)="viewRendezVous(rv)" style="padding: 4px 8px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Voir
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="editRendezVous(rv)" style="padding: 4px 8px; background-color: #ffc107; color: black; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Modifier
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="deleteRendezVous(rv)" style="padding: 4px 8px; background-color: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Supprimer
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal de création/édition de rendez-vous -->
    <div *ngIf="showCreateModal" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000; display: flex; justify-content: center; align-items: center;">
      <div style="background-color: white; padding: 30px; border-radius: 8px; width: 90%; max-width: 600px; max-height: 90vh; overflow-y: auto;">
        <h3 style="margin-top: 0; margin-bottom: 20px;">{{ editingRendezVous ? 'Modifier le rendez-vous' : 'Nouveau rendez-vous' }}</h3>
        
        <form style="display: flex; flex-direction: column; gap: 15px;">
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Patient *</label>
              <select 
                [(ngModel)]="rendezVousForm.patientId" 
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              >
                <option value="0">Sélectionner un patient</option>
                <option *ngFor="let patient of patients" [value]="patient.id">{{ patient.prenom }} {{ patient.nom }}</option>
              </select>
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Médecin *</label>
              <select 
                [(ngModel)]="rendezVousForm.medecinId" 
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              >
                <option value="0">Sélectionner un médecin</option>
                <option *ngFor="let medecin of medecins" [value]="medecin.id">Dr. {{ medecin.prenom }} {{ medecin.nom }} - {{ medecin.specialite }}</option>
              </select>
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Date et heure *</label>
              <input 
                [(ngModel)]="rendezVousForm.dateHeure" 
                type="datetime-local"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Salle *</label>
              <input 
                [(ngModel)]="rendezVousForm.salle" 
                placeholder="Salle A, Salle B, etc."
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Statut</label>
              <select 
                [(ngModel)]="rendezVousForm.statut" 
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              >
                <option value="PLANIFIE">Planifié</option>
                <option value="CONFIRME">Confirmé</option>
                <option value="ANNULE">Annulé</option>
                <option value="TERMINE">Terminé</option>
              </select>
            </div>
            <div></div>
          </div>

          <div>
            <label style="display: block; margin-bottom: 5px; font-weight: bold;">Notes</label>
            <textarea 
              [(ngModel)]="rendezVousForm.notes" 
              placeholder="Notes sur le rendez-vous"
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
              (click)="saveRendezVous()" 
              [disabled]="loading || !rendezVousForm.patientId || !rendezVousForm.medecinId || !rendezVousForm.dateHeure || !rendezVousForm.salle"
              style="padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
            >
              {{ loading ? 'Enregistrement...' : (editingRendezVous ? 'Modifier' : 'Créer') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class RendezVousComponent implements OnInit {
  private readonly rendezVousService = inject(RendezVousService);
  private readonly patientService = inject(PatientService);
  private readonly medecinService = inject(MedecinService);
  readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  
  rendezVous: RendezVous[] = [];
  patients: Patient[] = [];
  medecins: Medecin[] = [];
  loading = true;
  error = '';
  showCreateModal = false;
  editingRendezVous: RendezVous | null = null;
  rendezVousForm = {
    patientId: 0,
    medecinId: 0,
    dateHeure: '',
    salle: '',
    statut: 'PLANIFIE',
    notes: ''
  };

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.error = '';
    
    // Charger les rendez-vous, patients et médecins en parallèle
    Promise.all([
      this.rendezVousService.getAllRendezVous().toPromise(),
      this.patientService.getAllPatients().toPromise(),
      this.medecinService.getAllMedecins().toPromise()
    ]).then(([rendezVous, patients, medecins]) => {
      this.rendezVous = rendezVous || [];
      this.patients = patients || [];
      this.medecins = medecins || [];
      this.loading = false;
    }).catch((error) => {
      this.error = 'Erreur lors du chargement des données';
      this.loading = false;
      console.error('Erreur:', error);
    });
  }

  getPatientName(patientId: number): string {
    const patient = this.patients.find(p => p.id === patientId);
    return patient ? `${patient.prenom} ${patient.nom}` : 'Patient inconnu';
  }

  getMedecinName(medecinId: number): string {
    const medecin = this.medecins.find(m => m.id === medecinId);
    return medecin ? `Dr. ${medecin.prenom} ${medecin.nom}` : 'Médecin inconnu';
  }

  formatDateTime(dateTime: string): string {
    return this.rendezVousService.formatDateTime(dateTime);
  }

  getStatusColor(status: string): string {
    return this.rendezVousService.getStatusColor(status);
  }

  getStatusText(status: string): string {
    return this.rendezVousService.getStatusText(status);
  }

  createRendezVous() {
    this.showCreateModal = true;
    this.editingRendezVous = null;
    this.resetForm();
  }

  viewRendezVous(rendezVous: RendezVous) {
    // TODO: Implémenter la vue détaillée du rendez-vous
    console.log('Voir le rendez-vous:', rendezVous);
  }

  editRendezVous(rendezVous: RendezVous) {
    // TODO: Implémenter l'édition du rendez-vous
    console.log('Modifier le rendez-vous:', rendezVous);
  }

  deleteRendezVous(rendezVous: RendezVous) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer ce rendez-vous ?`)) {
      if (rendezVous.id) {
        this.rendezVousService.deleteRendezVous(rendezVous.id).subscribe({
          next: () => {
            this.loadData(); // Recharger la liste
          },
          error: (error) => {
            this.error = 'Erreur lors de la suppression du rendez-vous';
            console.error('Erreur:', error);
          }
        });
      }
    }
  }

  resetForm() {
    this.rendezVousForm = {
      patientId: 0,
      medecinId: 0,
      dateHeure: '',
      salle: '',
      statut: 'PLANIFIE',
      notes: ''
    };
  }

  saveRendezVous() {
    if (!this.rendezVousForm.patientId || !this.rendezVousForm.medecinId || !this.rendezVousForm.dateHeure || !this.rendezVousForm.salle) {
      this.error = 'Tous les champs obligatoires doivent être remplis';
      return;
    }

    const rendezVousData: RendezVous = {
      patientId: this.rendezVousForm.patientId,
      medecinId: this.rendezVousForm.medecinId,
      dateHeure: this.rendezVousForm.dateHeure,
      salle: this.rendezVousForm.salle,
      statut: this.rendezVousForm.statut,
      notes: this.rendezVousForm.notes || undefined
    };

    this.loading = true;
    this.error = '';

    this.rendezVousService.createRendezVous(rendezVousData).subscribe({
      next: (newRendezVous) => {
        this.loadData(); // Recharger la liste
        this.showCreateModal = false;
        this.resetForm();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors de la création du rendez-vous';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  closeModal() {
    this.showCreateModal = false;
    this.editingRendezVous = null;
    this.resetForm();
  }
}
