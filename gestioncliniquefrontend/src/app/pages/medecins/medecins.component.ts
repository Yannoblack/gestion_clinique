import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MedecinService, Medecin } from '../../services/medecin.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-medecins',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="max-width: 1200px; margin: 24px auto; padding: 0 16px;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
        <h2>Gestion des Médecins</h2>
        <div>
          <button (click)="createMedecin()" style="padding: 8px 16px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">
            Nouveau Médecin
          </button>
        </div>
      </div>
      
      <div *ngIf="loading" style="text-align: center; padding: 40px;">
        <div>Chargement des médecins...</div>
      </div>
      
      <div *ngIf="error" style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 4px; padding: 12px; margin-bottom: 16px;">
        {{ error }}
      </div>
      
      <div *ngIf="!loading && !error" style="background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden;">
        <div style="padding: 16px; background-color: #f8f9fa; border-bottom: 1px solid #dee2e6;">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0;">Liste des Médecins ({{ medecins.length }})</h3>
            <div>
              <input 
                [(ngModel)]="searchQuery" 
                (input)="searchMedecins()"
                placeholder="Rechercher un médecin..."
                style="padding: 8px; border: 1px solid #ccc; border-radius: 4px; width: 250px;"
              />
            </div>
          </div>
        </div>
        
        <div *ngIf="medecins.length === 0" style="padding: 40px; text-align: center; color: #6c757d;">
          Aucun médecin trouvé
        </div>
        
        <div *ngIf="medecins.length > 0" style="overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse;">
            <thead style="background-color: #f8f9fa;">
              <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Nom</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Prénom</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Spécialité</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">N° Ordre</th>
                <th style="padding: 12px; text-align: left; border-bottom: 2px solid #dee2e6;">Téléphone</th>
                <th style="padding: 12px; text-align: center; border-bottom: 2px solid #dee2e6;">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let medecin of medecins" style="border-bottom: 1px solid #dee2e6;">
                <td style="padding: 12px;">{{ medecin.nom }}</td>
                <td style="padding: 12px;">{{ medecin.prenom }}</td>
                <td style="padding: 12px;">{{ medecin.specialite }}</td>
                <td style="padding: 12px;">{{ medecin.numeroOrdre }}</td>
                <td style="padding: 12px;">{{ medecin.telephone }}</td>
                <td style="padding: 12px; text-align: center;">
                  <button (click)="viewMedecin(medecin)" style="padding: 4px 8px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Voir
                  </button>
                  <button (click)="editMedecin(medecin)" style="padding: 4px 8px; background-color: #ffc107; color: black; border: none; border-radius: 4px; cursor: pointer; margin-right: 4px;">
                    Modifier
                  </button>
                  <button (click)="deleteMedecin(medecin)" style="padding: 4px 8px; background-color: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    Supprimer
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal de création/édition de médecin -->
    <div *ngIf="showCreateModal" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); z-index: 1000; display: flex; justify-content: center; align-items: center;">
      <div style="background-color: white; padding: 30px; border-radius: 8px; width: 90%; max-width: 500px;">
        <h3 style="margin-top: 0; margin-bottom: 20px;">{{ editingMedecin ? 'Modifier le médecin' : 'Nouveau médecin' }}</h3>
        
        <form style="display: flex; flex-direction: column; gap: 15px;">
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Nom *</label>
              <input 
                [(ngModel)]="medecinForm.nom" 
                placeholder="Nom du médecin"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Prénom *</label>
              <input 
                [(ngModel)]="medecinForm.prenom" 
                placeholder="Prénom du médecin"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
                required
              />
            </div>
          </div>

          <div>
            <label style="display: block; margin-bottom: 5px; font-weight: bold;">Spécialité *</label>
            <input 
              [(ngModel)]="medecinForm.specialite" 
              placeholder="Spécialité médicale"
              style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              required
            />
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">N° Ordre</label>
              <input 
                [(ngModel)]="medecinForm.numeroOrdre" 
                placeholder="Numéro d'ordre"
                style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;"
              />
            </div>
            <div>
              <label style="display: block; margin-bottom: 5px; font-weight: bold;">Téléphone</label>
              <input 
                [(ngModel)]="medecinForm.telephone" 
                placeholder="0123456789"
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
              (click)="saveMedecin()" 
              [disabled]="loading || !medecinForm.nom || !medecinForm.prenom || !medecinForm.specialite"
              style="padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
            >
              {{ loading ? 'Enregistrement...' : (editingMedecin ? 'Modifier' : 'Créer') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class MedecinsComponent implements OnInit {
  private readonly medecinService = inject(MedecinService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  
  medecins: Medecin[] = [];
  filteredMedecins: Medecin[] = [];
  loading = true;
  error = '';
  searchQuery = '';
  showCreateModal = false;
  editingMedecin: Medecin | null = null;
  medecinForm = {
    nom: '',
    prenom: '',
    specialite: '',
    numeroOrdre: '',
    telephone: ''
  };

  ngOnInit() {
    this.loadMedecins();
  }

  loadMedecins() {
    this.loading = true;
    this.error = '';
    
    this.medecinService.getAllMedecins().subscribe({
      next: (data) => {
        this.medecins = data;
        this.filteredMedecins = [...data];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors du chargement des médecins';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  searchMedecins() {
    if (!this.searchQuery.trim()) {
      this.filteredMedecins = [...this.medecins];
    } else {
      this.filteredMedecins = this.medecinService.getMedecinsBySpecialite(this.medecins, this.searchQuery);
    }
  }

  createMedecin() {
    this.showCreateModal = true;
    this.editingMedecin = null;
    this.resetForm();
  }

  viewMedecin(medecin: Medecin) {
    // TODO: Implémenter la vue détaillée du médecin
    console.log('Voir le médecin:', medecin);
  }

  editMedecin(medecin: Medecin) {
    // TODO: Implémenter l'édition du médecin
    console.log('Modifier le médecin:', medecin);
  }

  deleteMedecin(medecin: Medecin) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le médecin Dr. ${medecin.prenom} ${medecin.nom} ?`)) {
      if (medecin.id) {
        this.medecinService.deleteMedecin(medecin.id).subscribe({
          next: () => {
            this.loadMedecins(); // Recharger la liste
          },
          error: (error) => {
            this.error = 'Erreur lors de la suppression du médecin';
            console.error('Erreur:', error);
          }
        });
      }
    }
  }

  resetForm() {
    this.medecinForm = {
      nom: '',
      prenom: '',
      specialite: '',
      numeroOrdre: '',
      telephone: ''
    };
  }

  saveMedecin() {
    if (!this.medecinForm.nom || !this.medecinForm.prenom || !this.medecinForm.specialite) {
      this.error = 'Le nom, prénom et spécialité sont obligatoires';
      return;
    }

    const medecinData: Medecin = {
      nom: this.medecinForm.nom,
      prenom: this.medecinForm.prenom,
      specialite: this.medecinForm.specialite,
      numeroOrdre: this.medecinForm.numeroOrdre,
      telephone: this.medecinForm.telephone
    };

    this.loading = true;
    this.error = '';

    this.medecinService.createMedecin(medecinData).subscribe({
      next: (newMedecin) => {
        this.loadMedecins(); // Recharger la liste
        this.showCreateModal = false;
        this.resetForm();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erreur lors de la création du médecin';
        this.loading = false;
        console.error('Erreur:', error);
      }
    });
  }

  closeModal() {
    this.showCreateModal = false;
    this.editingMedecin = null;
    this.resetForm();
  }
}
