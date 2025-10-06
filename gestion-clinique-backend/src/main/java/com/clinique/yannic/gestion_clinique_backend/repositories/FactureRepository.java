package com.clinique.yannic.gestion_clinique_backend.repositories;

import com.clinique.yannic.gestion_clinique_backend.models.Facture;
import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    // Récupérer toutes les factures associées à un patient via ses rendez-vous
    List<Facture> findByRendezVous_Patient(Patient patient);

}
