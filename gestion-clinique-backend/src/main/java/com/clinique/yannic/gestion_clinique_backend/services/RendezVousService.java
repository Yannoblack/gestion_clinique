package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.RendezVousDTO;
import com.clinique.yannic.gestion_clinique_backend.controllers.dto.FactureDTO;
import com.clinique.yannic.gestion_clinique_backend.exception.AppointmentConflictException;
import com.clinique.yannic.gestion_clinique_backend.exception.InvalidDateException;
import com.clinique.yannic.gestion_clinique_backend.exception.ResourceNotFoundException;
import com.clinique.yannic.gestion_clinique_backend.models.Medecin;
import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import com.clinique.yannic.gestion_clinique_backend.models.RendezVous;
import com.clinique.yannic.gestion_clinique_backend.repositories.FactureRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.MedecinRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.PatientRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final FactureRepository factureRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository,
                             PatientRepository patientRepository,
                             MedecinRepository medecinRepository,
                             FactureRepository factureRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.factureRepository = factureRepository;
    }

    // =========================
    // MÉTHODES PUBLIQUES
    // =========================

    /**
     * Récupérer tous les rendez-vous
     */
    @Transactional(readOnly = true)
    public List<RendezVousDTO> getAllRendezVous() {
        return rendezVousRepository.findAll()
                .stream()
                .map(RendezVousDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un rendez-vous par ID
     */
    @Transactional(readOnly = true)
    public RendezVousDTO getRendezVousById(Long id) {
        RendezVous rendezVous = findRendezVousById(id);
        return new RendezVousDTO(rendezVous);
    }

    /**
     * Créer un nouveau rendez-vous
     */
    public RendezVousDTO createRendezVous(RendezVousDTO dto) {
        // Log pour débogage
        System.out.println("=== DEBUG RendezVousService.createRendezVous ===");
        System.out.println("DTO reçu: " + dto);
        System.out.println("PatientId: " + dto.getPatientId());
        System.out.println("MedecinId: " + dto.getMedecinId());
        System.out.println("DateHeure: " + dto.getDateHeure());
        System.out.println("Salle: " + dto.getSalle());
        
        // Validation des données
        validateRendezVousDTO(dto);
        
        // Récupération des entités
        Patient patient = findPatientById(dto.getPatientId());
        Medecin medecin = findMedecinById(dto.getMedecinId());
        
        // Vérification des conflits
        checkAppointmentConflicts(dto.getPatientId(), dto.getMedecinId(), dto.getSalle(), dto.getDateHeure(), null);
        
        // Création du rendez-vous
        RendezVous rendezVous = new RendezVous();
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);
        rendezVous.setDateHeure(dto.getDateHeure());
        rendezVous.setSalle(dto.getSalle());
        rendezVous.setStatut(dto.getStatut() != null ? dto.getStatut() : "PLANIFIE");
        rendezVous.setNotes(dto.getNotes());
        
        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
        return new RendezVousDTO(savedRendezVous);
    }

    /**
     * Mettre à jour un rendez-vous
     */
    public RendezVousDTO updateRendezVous(Long id, RendezVousDTO dto) {
        // Validation des données
        validateRendezVousDTO(dto);
        
        // Récupération du rendez-vous existant
        RendezVous existingRendezVous = findRendezVousById(id);
        
        // Récupération des entités
        Patient patient = findPatientById(dto.getPatientId());
        Medecin medecin = findMedecinById(dto.getMedecinId());
        
        // Vérification des conflits (en excluant le rendez-vous actuel)
        checkAppointmentConflicts(dto.getPatientId(), dto.getMedecinId(), dto.getSalle(), dto.getDateHeure(), id);
        
        // Mise à jour du rendez-vous
        existingRendezVous.setPatient(patient);
        existingRendezVous.setMedecin(medecin);
        existingRendezVous.setDateHeure(dto.getDateHeure());
        existingRendezVous.setSalle(dto.getSalle());
        existingRendezVous.setStatut(dto.getStatut() != null ? dto.getStatut() : existingRendezVous.getStatut());
        existingRendezVous.setNotes(dto.getNotes());
        
        RendezVous updatedRendezVous = rendezVousRepository.save(existingRendezVous);
        return new RendezVousDTO(updatedRendezVous);
    }

    /**
     * Supprimer un rendez-vous
     */
    public void deleteRendezVous(Long id) {
        if (!rendezVousRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + id);
        }
        rendezVousRepository.deleteById(id);
    }

    /**
     * Récupérer les factures d'un patient
     */
    @Transactional(readOnly = true)
    public List<FactureDTO> getFacturesByPatientId(Long patientId) {
        Patient patient = findPatientById(patientId);
        return factureRepository.findByRendezVous_Patient(patient)
                .stream()
                .map(FactureDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les rendez-vous d'un patient
     */
    @Transactional(readOnly = true)
    public List<RendezVousDTO> getRendezVousByPatientId(Long patientId) {
        return rendezVousRepository.findByPatientId(patientId)
                .stream()
                .map(RendezVousDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les rendez-vous d'un médecin
     */
    @Transactional(readOnly = true)
    public List<RendezVousDTO> getRendezVousByMedecinId(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId)
                .stream()
                .map(RendezVousDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Vérifier la disponibilité d'un patient et d'un médecin
     */
    @Transactional(readOnly = true)
    public Map<String, Object> checkPatientAndMedecinAvailability(Long patientId, Long medecinId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Patient patient = findPatientById(patientId);
            result.put("patient", Map.of(
                "id", patient.getId(),
                "nom", patient.getNom(),
                "prenom", patient.getPrenom(),
                "exists", true
            ));
        } catch (ResourceNotFoundException e) {
            result.put("patient", Map.of(
                "id", patientId,
                "exists", false,
                "error", e.getMessage()
            ));
        }
        
        try {
            Medecin medecin = findMedecinById(medecinId);
            result.put("medecin", Map.of(
                "id", medecin.getId(),
                "nom", medecin.getNom(),
                "prenom", medecin.getPrenom(),
                "specialite", medecin.getSpecialite(),
                "exists", true
            ));
        } catch (ResourceNotFoundException e) {
            result.put("medecin", Map.of(
                "id", medecinId,
                "exists", false,
                "error", e.getMessage()
            ));
        }
        
        boolean patientExists = result.get("patient") instanceof Map && 
                               Boolean.TRUE.equals(((Map<?, ?>) result.get("patient")).get("exists"));
        boolean medecinExists = result.get("medecin") instanceof Map && 
                               Boolean.TRUE.equals(((Map<?, ?>) result.get("medecin")).get("exists"));
        boolean bothExist = patientExists && medecinExists;
        
        result.put("canCreateAppointment", bothExist);
        
        return result;
    }

    // =========================
    // MÉTHODES PRIVÉES
    // =========================

    /**
     * Trouver un rendez-vous par ID
     */
    private RendezVous findRendezVousById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));
    }

    /**
     * Trouver un patient par ID
     */
    private Patient findPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID : " + patientId + ". Veuillez vérifier que ce patient existe dans la base de données."));
    }

    /**
     * Trouver un médecin par ID
     */
    private Medecin findMedecinById(Long medecinId) {
        return medecinRepository.findById(medecinId)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID : " + medecinId + ". Veuillez vérifier que ce médecin existe dans la base de données."));
    }

    /**
     * Valider les données du DTO
     */
    private void validateRendezVousDTO(RendezVousDTO dto) {
        System.out.println("=== DEBUG validateRendezVousDTO ===");
        System.out.println("DTO: " + dto);
        
        if (dto == null) {
            System.out.println("ERREUR: DTO est null");
            throw new IllegalArgumentException("Le DTO du rendez-vous ne peut pas être null");
        }
        
        System.out.println("PatientId: " + dto.getPatientId());
        if (dto.getPatientId() == null) {
            System.out.println("ERREUR: PatientId est null");
            throw new IllegalArgumentException("L'ID du patient est obligatoire");
        }
        
        if (dto.getMedecinId() == null) {
            throw new IllegalArgumentException("L'ID du médecin est obligatoire");
        }
        
        if (dto.getDateHeure() == null) {
            throw new IllegalArgumentException("La date et l'heure sont obligatoires");
        }
        
        if (dto.getSalle() == null || dto.getSalle().trim().isEmpty()) {
            throw new IllegalArgumentException("La salle est obligatoire");
        }
        
        validateAppointmentDate(dto.getDateHeure());
    }

    /**
     * Valider la date du rendez-vous
     */
    private void validateAppointmentDate(LocalDateTime dateHeure) {
        if (dateHeure == null) {
            throw new InvalidDateException("La date et l'heure du rendez-vous sont obligatoires.");
        }

        // Validation plus flexible : permettre les rendez-vous jusqu'à 1 heure dans le passé
        // pour gérer les cas où l'utilisateur crée un rendez-vous juste après l'heure prévue
        LocalDateTime now = LocalDateTime.now().minusHours(1);
        if (dateHeure.isBefore(now)) {
            throw new InvalidDateException("Impossible de créer un rendez-vous plus d'une heure dans le passé. Date fournie: " + dateHeure + ", Date actuelle: " + LocalDateTime.now());
        }
    }

    /**
     * Vérifier les conflits de rendez-vous
     */
    private void checkAppointmentConflicts(Long patientId, Long medecinId, String salle, LocalDateTime dateHeure, Long excludeId) {
        // Vérifier les conflits de médecin
        List<RendezVous> medecinConflicts = rendezVousRepository.findMedecinConflicts(medecinId, dateHeure, excludeId);
        if (!medecinConflicts.isEmpty()) {
            throw new AppointmentConflictException(
                    String.format("Un rendez-vous existe déjà pour ce médecin le %s à %s",
                            dateHeure.toLocalDate(), dateHeure.toLocalTime())
            );
        }

        // Vérifier les conflits de patient
        List<RendezVous> patientConflicts = rendezVousRepository.findPatientConflicts(patientId, dateHeure, excludeId);
        if (!patientConflicts.isEmpty()) {
            throw new AppointmentConflictException(
                    String.format("Un rendez-vous existe déjà pour ce patient le %s à %s",
                            dateHeure.toLocalDate(), dateHeure.toLocalTime())
            );
        }

        // Vérifier les conflits de salle
        List<RendezVous> salleConflicts = rendezVousRepository.findSalleConflicts(salle, dateHeure, excludeId);
        if (!salleConflicts.isEmpty()) {
            throw new AppointmentConflictException(
                    String.format("La salle %s est déjà occupée le %s à %s",
                            salle, dateHeure.toLocalDate(), dateHeure.toLocalTime())
            );
        }
    }
}