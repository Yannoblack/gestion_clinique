package com.clinique.yannic.gestion_clinique_backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Le patient ne peut pas être null")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = false)
    @NotNull(message = "Le médecin ne peut pas être null")
    private Medecin medecin;

    @Column(name = "date_heure", nullable = false)
    @NotNull(message = "La date et heure du rendez-vous ne peuvent pas être null")
    private LocalDateTime dateHeure;

    @Column(name = "salle", nullable = false, length = 50)
    @NotBlank(message = "La salle est obligatoire")
    @Size(max = 50, message = "Le nom de la salle ne peut pas dépasser 50 caractères")
    private String salle;

    @Column(name = "statut", length = 20)
    @Size(max = 20, message = "Le statut ne peut pas dépasser 20 caractères")
    private String statut = "PLANIFIE";

    @Column(name = "notes", length = 500)
    @Size(max = 500, message = "Les notes ne peuvent pas dépasser 500 caractères")
    private String notes;

    @OneToOne(mappedBy = "rendezVous", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("rendezvous-facture")
    private Facture facture;

    // Constructeurs
    public RendezVous() {
    }

    public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, String salle) {
        this.patient = patient;
        this.medecin = medecin;
        this.dateHeure = dateHeure;
        this.salle = salle;
        this.statut = "PLANIFIE";
    }

    public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, String salle, String statut, String notes) {
        this.patient = patient;
        this.medecin = medecin;
        this.dateHeure = dateHeure;
        this.salle = salle;
        this.statut = statut;
        this.notes = notes;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getId() : null) +
                ", medecin=" + (medecin != null ? medecin.getId() : null) +
                ", dateHeure=" + dateHeure +
                ", salle='" + salle + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}