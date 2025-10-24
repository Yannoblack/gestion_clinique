package com.clinique.yannic.gestion_clinique_backend.controllers.dto;

import com.clinique.yannic.gestion_clinique_backend.models.RendezVous;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Schema(description = "DTO pour les rendez-vous")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RendezVousDTO {

    @Schema(description = "ID du rendez-vous (auto-généré)", example = "1")
    private Long id;

    @NotNull(message = "L'ID du patient est obligatoire")
    @JsonProperty("patientId")
    @Schema(description = "ID du patient", example = "1", required = true)
    private Long patientId;

    @NotNull(message = "L'ID du médecin est obligatoire")
    @JsonProperty("medecinId")
    @Schema(description = "ID du médecin", example = "1", required = true)
    private Long medecinId;

    @NotNull(message = "La date et heure sont obligatoires")
    @JsonProperty("dateHeure")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Date et heure du rendez-vous", example = "2025-12-25T10:00:00", type = "string", format = "date-time", required = true)
    private LocalDateTime dateHeure;

    @NotBlank(message = "La salle est obligatoire")
    @JsonProperty("salle")
    @Size(max = 50, message = "Le nom de la salle ne peut pas dépasser 50 caractères")
    @Schema(description = "Salle du rendez-vous", example = "Salle A", required = true)
    private String salle;

    @Size(max = 20, message = "Le statut ne peut pas dépasser 20 caractères")
    @JsonProperty("statut")
    @Schema(description = "Statut du rendez-vous", example = "PLANIFIE")
    private String statut;

    @Size(max = 500, message = "Les notes ne peuvent pas dépasser 500 caractères")
    @JsonProperty("notes")
    @Schema(description = "Notes du rendez-vous", example = "Consultation de routine")
    private String notes;

    // Constructeurs
    public RendezVousDTO() {
        this.statut = "PLANIFIE";
    }

    public RendezVousDTO(RendezVous rendezVous) {
        this.id = rendezVous.getId();
        this.patientId = rendezVous.getPatient() != null ? rendezVous.getPatient().getId() : null;
        this.medecinId = rendezVous.getMedecin() != null ? rendezVous.getMedecin().getId() : null;
        this.dateHeure = rendezVous.getDateHeure();
        this.salle = rendezVous.getSalle();
        this.statut = rendezVous.getStatut();
        this.notes = rendezVous.getNotes();
    }

    public RendezVousDTO(Long id, Long patientId, Long medecinId, LocalDateTime dateHeure, String salle) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.dateHeure = dateHeure;
        this.salle = salle;
        this.statut = "PLANIFIE";
    }

    public RendezVousDTO(Long id, Long patientId, Long medecinId, LocalDateTime dateHeure, String salle, String statut, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(Long medecinId) {
        this.medecinId = medecinId;
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

    @Override
    public String toString() {
        return "RendezVousDTO{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", medecinId=" + medecinId +
                ", dateHeure=" + dateHeure +
                ", salle='" + salle + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}