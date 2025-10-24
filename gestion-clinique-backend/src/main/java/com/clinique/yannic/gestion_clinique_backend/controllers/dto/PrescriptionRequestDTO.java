package com.clinique.yannic.gestion_clinique_backend.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrescriptionRequestDTO {

    @JsonProperty("idPatient")
    @NotNull(message = "L'ID du patient est obligatoire")
    private Long idPatient;
    
    @JsonProperty("idRendezVous")
    @NotNull(message = "L'ID du rendez-vous est obligatoire")
    private Long idRendezVous;
    
    @JsonProperty("idMedecin")
    @NotNull(message = "L'ID du médecin est obligatoire")
    private Long idMedecin;
    @JsonProperty("medicaments")
    private String medicaments;
    
    @JsonProperty("instructions")
    private String instructions;
    
    @JsonProperty("autresInformations")
    private String autresInformations;

    public PrescriptionRequestDTO() {
    }

    // Getters et setters

    public Long getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(Long idPatient) {
        this.idPatient = idPatient;
    }

    public Long getIdRendezVous() {
        return idRendezVous;
    }

    public void setIdRendezVous(Long idRendezVous) {
        this.idRendezVous = idRendezVous;
    }

    public Long getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(Long idMedecin) {
        this.idMedecin = idMedecin;
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
}
