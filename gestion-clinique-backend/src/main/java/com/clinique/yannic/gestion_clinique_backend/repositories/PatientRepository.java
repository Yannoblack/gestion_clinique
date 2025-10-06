package com.clinique.yannic.gestion_clinique_backend.repositories;

import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByNumeroSecuriteSociale(String numeroSecuriteSociale);

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Patient> searchByNomOrPrenom(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Patient p ORDER BY p.nom, p.prenom")
    List<Patient> findAllOrderByNomPrenom();

    boolean existsByEmail(String email);

    boolean existsByNumeroSecuriteSociale(String numeroSecuriteSociale);
}
