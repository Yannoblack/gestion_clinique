import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

// Interfaces pour les modèles
export interface Patient {
  id?: number;
  nom: string;
  prenom: string;
  telephone?: string;
  email?: string;
  adresse?: string;
  dateNaissance?: string;
  numeroSecuriteSociale?: string;
  antecedents?: string;
  allergies?: string;
}

export interface Medecin {
  id?: number;
  nom: string;
  prenom: string;
  specialite: string;
  numeroOrdre: string;
  telephone: string;
}

export interface RendezVous {
  id?: number;
  patientId: number;
  medecinId: number;
  dateHeure: string;
  salle: string;
  statut?: string;
  notes?: string;
}

export interface Facture {
  id?: number;
  montant: number;
  dateFacture: string;
  statut: string;
  rendezVousId?: number;
}

export interface Prescription {
  id?: number;
  idPatient: number;
  idRendezVous?: number;
  idMedecin: number;
  medicaments: string;
  instructions: string;
  autresInformations?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  role?: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  // =========================
  // AUTHENTIFICATION
  // =========================

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, { username, password });
  }

  register(username: string, password: string, role: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/register`, { username, password, role });
  }

  // =========================
  // PATIENTS
  // =========================

  getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${this.baseUrl}/api/patients`);
  }

  getPatientById(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.baseUrl}/api/patients/${id}`);
  }

  searchPatients(query: string): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${this.baseUrl}/api/patients/search?q=${query}`);
  }

  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(`${this.baseUrl}/api/patients`, patient);
  }

  updatePatient(id: number, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.baseUrl}/api/patients/${id}`, patient);
  }

  deletePatient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/patients/${id}`);
  }

  // =========================
  // MEDECINS
  // =========================

  getMedecins(): Observable<Medecin[]> {
    return this.http.get<Medecin[]>(`${this.baseUrl}/api/medecins`);
  }

  getMedecinById(id: number): Observable<Medecin> {
    return this.http.get<Medecin>(`${this.baseUrl}/api/medecins/${id}`);
  }

  createMedecin(medecin: Medecin): Observable<Medecin> {
    return this.http.post<Medecin>(`${this.baseUrl}/api/medecins`, medecin);
  }

  updateMedecin(id: number, medecin: Medecin): Observable<Medecin> {
    return this.http.put<Medecin>(`${this.baseUrl}/api/medecins/${id}`, medecin);
  }

  deleteMedecin(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/medecins/${id}`);
  }

  // =========================
  // RENDEZ-VOUS
  // =========================

  getRendezVous(): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.baseUrl}/rendezvous`);
  }

  getRendezVousById(id: number): Observable<RendezVous> {
    return this.http.get<RendezVous>(`${this.baseUrl}/rendezvous/${id}`);
  }

  getRendezVousByPatientId(patientId: number): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.baseUrl}/rendezvous/patient/${patientId}`);
  }

  getRendezVousByMedecinId(medecinId: number): Observable<RendezVous[]> {
    return this.http.get<RendezVous[]>(`${this.baseUrl}/rendezvous/medecin/${medecinId}`);
  }

  createRendezVous(rendezVous: RendezVous): Observable<RendezVous> {
    return this.http.post<RendezVous>(`${this.baseUrl}/rendezvous`, rendezVous);
  }



  updateRendezVous(id: number, rendezVous: RendezVous): Observable<RendezVous> {
    return this.http.put<RendezVous>(`${this.baseUrl}/rendezvous/${id}`, rendezVous);
  }

  deleteRendezVous(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/rendezvous/${id}`);
  }

  checkAvailability(patientId: number, medecinId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/rendezvous/check-availability?patientId=${patientId}&medecinId=${medecinId}`);
  }

  // =========================
  // FACTURES
  // =========================

  getFactures(): Observable<Facture[]> {
    return this.http.get<Facture[]>(`${this.baseUrl}/factures`);
  }

  getFactureById(id: number): Observable<Facture> {
    return this.http.get<Facture>(`${this.baseUrl}/factures/${id}`);
  }

  getFacturesByPatientId(patientId: number): Observable<Facture[]> {
    return this.http.get<Facture[]>(`${this.baseUrl}/rendezvous/patient/${patientId}/factures`);
  }

  createFacture(facture: Facture): Observable<Facture> {
    return this.http.post<Facture>(`${this.baseUrl}/factures`, facture);
  }

  updateFacture(id: number, facture: Facture): Observable<Facture> {
    return this.http.put<Facture>(`${this.baseUrl}/factures/${id}`, facture);
  }

  deleteFacture(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/factures/${id}`);
  }

  linkFactureToRendezVous(factureId: number, rendezVousId: number): Observable<Facture> {
    return this.http.put<Facture>(`${this.baseUrl}/factures/${factureId}/rendezvous/${rendezVousId}`, {});
  }

  // =========================
  // PRESCRIPTIONS
  // =========================

  getPrescriptions(): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.baseUrl}/prescriptions`);
  }

  getPrescriptionById(id: number): Observable<Prescription> {
    return this.http.get<Prescription>(`${this.baseUrl}/prescriptions/${id}`);
  }

  getPrescriptionsByRendezVous(rendezVousId: number): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.baseUrl}/prescriptions/rendezVous/${rendezVousId}`);
  }

  getPrescriptionsByMedecin(medecinId: number): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.baseUrl}/prescriptions/medecin/${medecinId}`);
  }

  createPrescription(prescription: Prescription): Observable<Prescription> {
    return this.http.post<Prescription>(`${this.baseUrl}/prescriptions`, prescription);
  }

  updatePrescription(id: number, prescription: Prescription): Observable<Prescription> {
    return this.http.put<Prescription>(`${this.baseUrl}/prescriptions/${id}`, prescription);
  }

  deletePrescription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/prescriptions/${id}`);
  }
}



