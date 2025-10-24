import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientService, Patient } from '../../services/patient.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-patients',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="max-width: 1200px; margin: 24px auto; padding: 0 16px;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <h2>{{ authService.isPatient() ? 'Mes Informations' : 'Gestion des Patients' }}</h2>
        <div>
          <button (click)="logout()" style="padding: 8px 16px; background-color: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 8px;">
            Déconnexion
          </button>
          <button *ngIf="!authService.isPatient()" (click)="createPatient()" style="padding: 8px 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">
            Nouveau Patient
          </button>
        </div>
      </div>
      
      <div *ngIf="loading" style="text-align: center; padding: 40px;">
        <div>Chargement des patients...</div>
      </div>
      
      <div *ngIf="error" style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px; padding: 12px; margin-bottom: 16px;">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error" style="background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
        <div style="padding: 16px; background-color: #f8f9fa; border-bottom: 1px solid #dee2e6;">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0;">Liste des Patients ({{ patients.length }})</h3>
            <div *ngIf="!authService.isPatient()">
              <input 
                [(ngModel)]="searchQuery" 
                (input)="searchPatients()"
                placeholder="Rechercher un patient..."
                style="padding: 8px; border: 1px solid #ccc; border-radius: 4px; width: 250px;"
              />
            </div>
          </div>
        </div>
        
        <div *ngIf="patients.length === 0" style="padding: 40px; text-align: center; color: #6c757d;">
          Aucun patient trouvé
        </div>
        
        <div *ngIf="patients.length > 0" style="overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse;">
            <thead style="background-color: #f8f9fa;">
              <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Nom</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Prénom</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Téléphone</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Email</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Date de naissance</th>
                <th style="padding: 12px; text-align: center; border-bottom: 2px solid #dee2e6;">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let patient of patients" style="border-bottom: 1px solid #dee2e6;">
                <td style="padding: 12px;">{{ patient.nom }}</td>
                <td style="padding: 12px;">{{ patient.prenom }}</td>
                <td style="padding: 12px;">{{ patient.telephone || '-' }}</td>
                <td style="padding: 12px;">{{ patient.email || '-' }}</td>
                <td style="padding: 12px;">{{ formatDate(patient.dateNaissance) }}</td>
                <td style="padding: 12px; text-align: center;">
                  <button (click)="viewPatient(patient)" style="padding: 4px 8px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Voir
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="editPatient(patient)" style="padding: 4px 8px; background-color: #ffc107; color: black; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Modifier
                  </button>
                  <button *ngIf="!authService.isPatient()" (click)="deletePatient(patient)" style="padding: 4px 8px; background-color: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Supprimer
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal de création/édition de patient -->
    <div *ngIf="showCreateModal" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000; display: flex; justify-content: center; align-items: center;">
      <div style="background-color: white; padding: 30px; border-radius: 8px; width: 90%; max-width: 600px; max-height: 90vh; overflow-y: auto;">
        <h3 style="margin-top: 0; margin-bottom: 20px;">{{ editingPatient ? 'Modifier le patient' : 'Nouveau patient' }}</h3>
        
        <form style="display: flex; flex-direction: column; gap: 15px;">
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Nom *</label>
              <input 
                [(ngModel)]="patientForm.nom" 
                placeholder="Nom du patient"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Prénom *</label>
              <input 
                [(ngModel)]="patientForm.prenom" 
                placeholder="Prénom du patient"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Email</label>
              <input 
                [(ngModel)]="patientForm.email" 
                type="email"
                placeholder="email@example.com"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Téléphone</label>
              <input 
                [(ngModel)]="patientForm.telephone" 
                placeholder="0123456789"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
          </div>

          <div>
            <label style="display: block; margin-bottom: 5px; font-weight: bold;">Adresse</label>
            <textarea 
              [(ngModel)]="patientForm.adresse" 
              placeholder="Adresse complète"
              style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; min-height: 60px; resize: vertical;"
            ></textarea>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Date de naissance</label>
              <input 
                [(ngModel)]="patientForm.dateNaissance" 
                type="date"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">N° Sécurité Sociale</label>
              <input 
                [(ngModel)]="patientForm.numeroSecuriteSociale" 
                placeholder="1234567890123"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
          </div>

          <div>
            <label style="display: block; margin-bottom: 5px; font-weight: bold;">Antécédents médicaux</label>
            <textarea 
              [(ngModel)]="patientForm.antecedents" 
              placeholder="Antécédents médicaux du patient"
              style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; min-height: 60px; resize: vertical;"
            ></textarea>
          </div>

          <div>
            <label style="display: block; margin-bottom: 5px; font-weight: bold;">Allergies</label>
            <textarea 
              [(ngModel)]="patientForm.allergies" 
              placeholder="Allergies connues du patient"
              style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; min-height: 60px; resize: vertical;"
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
              (click)="savePatient()" 
              [disabled]="loading || !patientForm.nom || !patientForm.prenom"
              style="padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
            >
              {{ loading ? 'Enregistrement...' : (editingPatient ? 'Modifier' : 'Créer') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class PatientsComponent implements OnInit {
  private readonly patientService = inject(PatientService);
  readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  
  patients: Patient[] = [];
  filteredPatients: Patient[] = [];
  loading = true;
  error = '';
  searchQuery = '';
  showCreateModal = false;
  editingPatient: Patient | null = null;
  patientForm = {
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    adresse: '',
    dateNaissance: '',
    numeroSecuriteSociale: '',
    antecedents: '',
    allergies: ''
  };

  ngOnInit() {
    this.loadPatients();
  }

  loadPatients() {
    this.loading = true;
    this.error = '';
    
    this.patientService.getAllPatients().subscribe({
      next: (data) => {
        this.patients = data;
        this.filteredPatients = [...data];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des patients';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  searchPatients() {
    if (!this.searchQuery.trim()) {
      this.filteredPatients = [...this.patients];
    } else {
      this.patientService.searchPatients(this.searchQuery).subscribe({
        next: (data) => {
          this.filteredPatients = data;
        },
        error: (error) => {
          console.error('Erreur de recherche:', error);
        }
      });
    }
  }

  createPatient() {
    this.showCreateModal = true;
    this.editingPatient = null;
    this.resetForm();
  }

  viewPatient(patient: Patient) {
    // TODO: Implémenter la vue détaillée du patient
    console.log('Voir le patient:', patient);
  }

  editPatient(patient: Patient) {
    // TODO: Implémenter l'édition du patient
    console.log('Modifier le patient:', patient);
  }

  deletePatient(patient: Patient) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le patient ${patient.prenom} ${patient.nom} ?`)) {
      if (patient.id) {
        this.patientService.deletePatient(patient.id).subscribe({
          next: () => {
            this.loadPatients(); // Recharger la liste
          },
          error: (error) => {
            this.error = 'Erreur lors de la suppression du patient';
            console.error('Erreur:', error);
          }
        });
      }
    }
  }

  logout() {
    this.authService.logout();
  }

  formatDate(date: string | undefined): string {
    if (!date) return '-';
    return new Date(date).toLocaleDateString('fr-FR');
  }

  resetForm() {
    this.patientForm = {
      nom: '',
      prenom: '',
      email: '',
      telephone: '',
      adresse: '',
      dateNaissance: '',
      numeroSecuriteSociale: '',
      antecedents: '',
      allergies: ''
    };
  }

  savePatient() {
    if (!this.patientForm.nom || !this.patientForm.prenom) {
      this.error = 'Le nom et le prénom sont obligatoires';
      return;
    }

    const patientData: Patient = {
      nom: this.patientForm.nom,
      prenom: this.patientForm.prenom,
      email: this.patientForm.email || undefined,
      telephone: this.patientForm.telephone || undefined,
      adresse: this.patientForm.adresse || undefined,
      dateNaissance: this.patientForm.dateNaissance || undefined,
      numeroSecuriteSociale: this.patientForm.numeroSecuriteSociale || undefined,
      antecedents: this.patientForm.antecedents || undefined,
      allergies: this.patientForm.allergies || undefined
    };

    this.loading = true;
    this.error = '';

    this.patientService.createPatient(patientData).subscribe({
      next: (newPatient) => {
        this.loadPatients(); // Recharger la liste
        this.showCreateModal = false;
        this.resetForm();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors de la création du patient';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  closeModal() {
    this.showCreateModal = false;
    this.editingPatient = null;
    this.resetForm();
  }
}


