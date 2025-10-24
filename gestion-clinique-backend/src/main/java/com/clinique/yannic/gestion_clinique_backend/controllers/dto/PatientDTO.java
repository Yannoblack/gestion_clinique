package com.clinique.yannic.gestion_clinique_backend.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    @JsonProperty("nom")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    @JsonProperty("prenom")
    private String prenom;

    @Email(message = "Format d'email invalide")
    @Size(max = 150)
    @JsonProperty("email")
    private String email;

    @Size(max = 15)
    @JsonProperty("telephone")
    private String telephone;

    @PastOrPresent(message = "La date de naissance ne peut pas être dans le futur")
    @JsonProperty("dateNaissance")
    private LocalDate dateNaissance;

    @Size(max = 500)
    @JsonProperty("adresse")
    private String adresse;

    @Size(max = 20)
    @JsonProperty("numeroSecuriteSociale")
    private String numeroSecuriteSociale;

    @JsonProperty("antecedents")
    private String antecedents;

    @JsonProperty("allergies")
    private String allergies;

    // Constructeur vide
    public PatientDTO() {
    }

    // Constructeur avec tous les champs sauf id
    public PatientDTO(String nom, String prenom, String email, String adresse, String telephone,
                      LocalDate dateNaissance, String numeroSecuriteSociale, String antecedents, String allergies) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
        this.numeroSecuriteSociale = numeroSecuriteSociale;
        this.antecedents = antecedents;
        this.allergies = allergies;
    }

    // Getters et setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroSecuriteSociale() {
        return numeroSecuriteSociale;
    }

    public void setNumeroSecuriteSociale(String numeroSecuriteSociale) {
        this.numeroSecuriteSociale = numeroSecuriteSociale;
    }

    public String getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
