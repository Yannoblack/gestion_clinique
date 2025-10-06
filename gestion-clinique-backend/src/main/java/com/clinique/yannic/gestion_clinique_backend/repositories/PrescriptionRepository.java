package com.clinique.yannic.gestion_clinique_backend.repositories;

import com.clinique.yannic.gestion_clinique_backend.models.Medecin;
import com.clinique.yannic.gestion_clinique_backend.models.Prescription;
import com.clinique.yannic.gestion_clinique_backend.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByRendezVous(RendezVous rendezVous);
    List<Prescription> findByMedecin(Medecin medecin);
    List<Prescription> findByRendezVousId(Long rendezVousId);
    List<Prescription> findByMedecinId(Long medecinId);
}


