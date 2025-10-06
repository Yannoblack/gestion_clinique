package com.clinique.yannic.gestion_clinique_backend.repositories;

import com.clinique.yannic.gestion_clinique_backend.models.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire
}
