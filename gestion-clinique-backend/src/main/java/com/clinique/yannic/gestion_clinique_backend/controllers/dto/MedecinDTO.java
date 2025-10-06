package com.clinique.yannic.gestion_clinique_backend.controllers.dto;

import jakarta.validation.constraints.*;

public class MedecinDTO {

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50)
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 50)
    private String prenom;

    @NotBlank(message = "La spécialité ne peut pas être vide")
    @Size(max = 100)
    private String specialite;

    @Pattern(regexp = "^[0-9]{8,11}$", message = "Le numéro d'ordre doit contenir entre 8 et 11 chiffres")
    private String numeroOrdre;

    @Pattern(regexp = "^[0-9]{10}$", message = "Le numéro de téléphone doit contenir exactement 10 chiffres")
    private String telephone;

    // Getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getNumeroOrdre() { return numeroOrdre; }
    public void setNumeroOrdre(String numeroOrdre) { this.numeroOrdre = numeroOrdre; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
