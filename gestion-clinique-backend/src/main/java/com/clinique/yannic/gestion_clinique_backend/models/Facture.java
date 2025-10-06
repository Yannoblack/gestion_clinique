package com.clinique.yannic.gestion_clinique_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(nullable = false)
    private LocalDate dateFacture;

    @Column(nullable = false)
    private String statut; // Ex: "PAYÉE", "NON_PAYÉE"

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rendezvous_id", unique = true) // Une facture est liée à un seul rendez-vous
    @JsonBackReference("rendezvous-facture") // Pour éviter les boucles de sérialisation
    private RendezVous rendezVous;

    // Constructeurs

    public Facture() {
    }

    public Facture(Long id, BigDecimal montant, LocalDate dateFacture, String statut, RendezVous rendezVous) {
        this.id = id;
        this.montant = montant;
        this.dateFacture = dateFacture;
        this.statut = statut;
        this.rendezVous = rendezVous;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public RendezVous getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.rendezVous = rendezVous;
    }

    // Nouvelle méthode pour obtenir un numéro de facture

    public String getNumeroFacture() {
        if (this.id != null) {
            return "FACT-" + this.id;
        } else {
            return "FACT-UNKNOWN";
        }
    }

    // equals & hashCode basés sur id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facture)) return false;
        Facture facture = (Facture) o;
        return id != null && id.equals(facture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString pour debug

    @Override
    public String toString() {
        return "Facture{" +
                "id=" + id +
                ", montant=" + montant +
                ", dateFacture=" + dateFacture +
                ", statut='" + statut + '\'' +
                ", rendezVousId=" + (rendezVous != null ? rendezVous.getId() : null) +
                '}';
    }
}
