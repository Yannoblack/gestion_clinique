package com.clinique.yannic.gestion_clinique_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medecins")
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @Column(nullable = false)
    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    @Column(nullable = false)
    @NotBlank(message = "La spécialité ne peut pas être vide")
    @Size(max = 100, message = "La spécialité ne peut pas dépasser 100 caractères")
    private String specialite;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{8,11}$", message = "Le numéro d'ordre doit contenir entre 8 et 11 chiffres")
    private String numeroOrdre;

    @Pattern(regexp = "^[0-9]{10}$", message = "Le numéro de téléphone doit contenir exactement 10 chiffres")
    private String telephone;

    @OneToMany(
            mappedBy = "medecin",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<RendezVous> rendezVousList = new ArrayList<>();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public List<RendezVous> getRendezVousList() { return rendezVousList; }
    public void setRendezVousList(List<RendezVous> rendezVousList) { this.rendezVousList = rendezVousList; }
}
