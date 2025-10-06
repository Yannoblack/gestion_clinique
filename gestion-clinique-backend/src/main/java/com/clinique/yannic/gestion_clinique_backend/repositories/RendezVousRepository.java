package com.clinique.yannic.gestion_clinique_backend.repositories;

import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import com.clinique.yannic.gestion_clinique_backend.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    // Récupérer les rendez-vous d'un patient
    List<RendezVous> findByPatient(Patient patient);

    // Récupérer les rendez-vous d'un patient par ID
    List<RendezVous> findByPatientId(Long patientId);

    // Récupérer les rendez-vous d'un médecin par ID
    List<RendezVous> findByMedecinId(Long medecinId);

    // Vérifier les conflits pour un médecin à une date/heure précise
    List<RendezVous> findByMedecinIdAndDateHeure(Long medecinId, LocalDateTime dateHeure);

    // Vérifier les conflits pour un patient à une date/heure précise
    List<RendezVous> findByPatientIdAndDateHeure(Long patientId, LocalDateTime dateHeure);

    // Vérifier les conflits de salle à une date/heure précise
    List<RendezVous> findBySalleAndDateHeure(String salle, LocalDateTime dateHeure);

    // Vérifier les conflits pour un médecin en excluant un rendez-vous spécifique
    List<RendezVous> findByMedecinIdAndDateHeureAndIdNot(Long medecinId, LocalDateTime dateHeure, Long excludeRendezVousId);

    // Vérifier les conflits pour un patient en excluant un rendez-vous spécifique
    List<RendezVous> findByPatientIdAndDateHeureAndIdNot(Long patientId, LocalDateTime dateHeure, Long excludeRendezVousId);

    // Vérifier les conflits de salle en excluant un rendez-vous spécifique
    List<RendezVous> findBySalleAndDateHeureAndIdNot(String salle, LocalDateTime dateHeure, Long excludeRendezVousId);

    // Récupérer tous les rendez-vous futurs d'un médecin
    List<RendezVous> findByMedecinIdAndDateHeureAfter(Long medecinId, LocalDateTime dateHeure);

    // Récupérer tous les rendez-vous d'un patient dans le futur
    List<RendezVous> findByPatientIdAndDateHeureAfter(Long patientId, LocalDateTime dateHeure);

    // Récupérer les rendez-vous par statut
    List<RendezVous> findByStatut(String statut);

    // Récupérer les rendez-vous d'un médecin par statut
    List<RendezVous> findByMedecinIdAndStatut(Long medecinId, String statut);

    // Récupérer les rendez-vous d'un patient par statut
    List<RendezVous> findByPatientIdAndStatut(Long patientId, String statut);

    // Requête personnalisée pour vérifier les conflits multiples
    @Query("SELECT r FROM RendezVous r WHERE " +
           "(r.medecin.id = :medecinId OR r.patient.id = :patientId OR r.salle = :salle) " +
           "AND r.dateHeure = :dateHeure " +
           "AND (:excludeId IS NULL OR r.id != :excludeId)")
    List<RendezVous> findConflicts(@Param("medecinId") Long medecinId, 
                                   @Param("patientId") Long patientId, 
                                   @Param("salle") String salle, 
                                   @Param("dateHeure") LocalDateTime dateHeure, 
                                   @Param("excludeId") Long excludeId);

    // Requête pour vérifier les conflits de médecin uniquement
    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.dateHeure = :dateHeure AND (:excludeId IS NULL OR r.id != :excludeId)")
    List<RendezVous> findMedecinConflicts(@Param("medecinId") Long medecinId, 
                                         @Param("dateHeure") LocalDateTime dateHeure, 
                                         @Param("excludeId") Long excludeId);

    // Requête pour vérifier les conflits de patient uniquement
    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :patientId AND r.dateHeure = :dateHeure AND (:excludeId IS NULL OR r.id != :excludeId)")
    List<RendezVous> findPatientConflicts(@Param("patientId") Long patientId, 
                                         @Param("dateHeure") LocalDateTime dateHeure, 
                                         @Param("excludeId") Long excludeId);

    // Requête pour vérifier les conflits de salle uniquement
    @Query("SELECT r FROM RendezVous r WHERE r.salle = :salle AND r.dateHeure = :dateHeure AND (:excludeId IS NULL OR r.id != :excludeId)")
    List<RendezVous> findSalleConflicts(@Param("salle") String salle, 
                                       @Param("dateHeure") LocalDateTime dateHeure, 
                                       @Param("excludeId") Long excludeId);

    // Récupérer les rendez-vous dans une plage de dates
    @Query("SELECT r FROM RendezVous r WHERE r.dateHeure BETWEEN :startDate AND :endDate ORDER BY r.dateHeure")
    List<RendezVous> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);

    // Récupérer les rendez-vous d'un médecin dans une plage de dates
    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.dateHeure BETWEEN :startDate AND :endDate ORDER BY r.dateHeure")
    List<RendezVous> findByMedecinAndDateRange(@Param("medecinId") Long medecinId, 
                                              @Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);

    // Récupérer les rendez-vous d'un patient dans une plage de dates
    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :patientId AND r.dateHeure BETWEEN :startDate AND :endDate ORDER BY r.dateHeure")
    List<RendezVous> findByPatientAndDateRange(@Param("patientId") Long patientId, 
                                              @Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
}