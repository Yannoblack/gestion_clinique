package com.clinique.yannic.gestion_clinique_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rendezVous_id")
    private RendezVous rendezVous;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    @Column(columnDefinition = "TEXT")
    private String medicaments;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String autresInformations;

    private LocalDateTime datePrescription;

    public Prescription() {
    }

    public Prescription(RendezVous rendezVous, Medecin medecin, String medicaments, String instructions, String autresInformations, LocalDateTime datePrescription) {
        this.rendezVous = rendezVous;
        this.medecin = medecin;
        this.medicaments = medicaments;
        this.instructions = instructions;
        this.autresInformations = autresInformations;
        this.datePrescription = datePrescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RendezVous getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.rendezVous = rendezVous;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public String getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(String medicaments) {
        this.medicaments = medicaments;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getAutresInformations() {
        return autresInformations;
    }

    public void setAutresInformations(String autresInformations) {
        this.autresInformations = autresInformations;
    }

    public LocalDateTime getDatePrescription() {
        return datePrescription;
    }

    public void setDatePrescription(LocalDateTime datePrescription) {
        this.datePrescription = datePrescription;
    }

    public void setPatient(Patient patient) {
    }
}
