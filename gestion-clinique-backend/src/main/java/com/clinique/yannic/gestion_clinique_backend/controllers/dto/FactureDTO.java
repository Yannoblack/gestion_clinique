package com.clinique.yannic.gestion_clinique_backend.controllers.dto;

import com.clinique.yannic.gestion_clinique_backend.models.Facture;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FactureDTO {

    @JsonProperty(access = Access.READ_ONLY)
    @Schema(description = "Identifiant généré automatiquement", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private BigDecimal montant;
    private LocalDate dateFacture;
    private String statut;
    private Long rendezVousId;

    // Constructeur vide (obligatoire pour Jackson)
    public FactureDTO() {}

    // Constructeur complet
    public FactureDTO(Long id, BigDecimal montant, LocalDate dateFacture, String statut, Long rendezVousId) {
        this.id = id;
        this.montant = montant;
        this.dateFacture = dateFacture;
        this.statut = statut;
        this.rendezVousId = rendezVousId;
    }

    // ✅ Constructeur qui prend une entité Facture
    public FactureDTO(Facture facture) {
        this.id = facture.getId();
        this.montant = facture.getMontant();
        this.dateFacture = facture.getDateFacture();
        this.statut = facture.getStatut();
        this.rendezVousId = (facture.getRendezVous() != null) ? facture.getRendezVous().getId() : null;
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public LocalDate getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Long getRendezVousId() { return rendezVousId; }
    public void setRendezVousId(Long rendezVousId) { this.rendezVousId = rendezVousId; }
}
