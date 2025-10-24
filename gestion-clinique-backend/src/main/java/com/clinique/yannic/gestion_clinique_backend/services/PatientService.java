package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.exception.*;
import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import com.clinique.yannic.gestion_clinique_backend.repositories.PatientRepository;
import com.clinique.yannic.gestion_clinique_backend.controllers.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient creerPatient(PatientDTO patientDTO) {
        // Vérifier l'unicité de l'email seulement si fourni
        if (patientDTO.getEmail() != null && !patientDTO.getEmail().trim().isEmpty() 
            && patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new DuplicateResourceException("Un patient avec cet email existe déjà");
        }
        
        // Vérifier l'unicité du numéro de sécurité sociale seulement si fourni
        if (patientDTO.getNumeroSecuriteSociale() != null && !patientDTO.getNumeroSecuriteSociale().trim().isEmpty()
            && patientRepository.existsByNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale())) {
            throw new DuplicateResourceException("Un patient avec ce numéro de sécurité sociale existe déjà");
        }

        Patient patient = new Patient();
        patient.setNom(patientDTO.getNom());
        patient.setPrenom(patientDTO.getPrenom());
        patient.setEmail(patientDTO.getEmail());
        patient.setTelephone(patientDTO.getTelephone());
        patient.setDateNaissance(patientDTO.getDateNaissance());
        patient.setAdresse(patientDTO.getAdresse());
        patient.setNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale());
        patient.setAntecedents(patientDTO.getAntecedents());
        patient.setAllergies(patientDTO.getAllergies());

        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public List<Patient> obtenirTousLesPatients() {
        return patientRepository.findAllOrderByNomPrenom();
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> obtenirTousLesPatientsDTO() {
        List<Patient> patients = obtenirTousLesPatients();
        return patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Patient obtenirPatientParId(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouvé avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Patient> obtenirPatientParEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Patient> rechercherPatients(String searchTerm) {
        return patientRepository.searchByNomOrPrenom(searchTerm);
    }

    public Patient modifierPatient(Long id, PatientDTO patientDTO) {
        Patient patient = obtenirPatientParId(id);

        if (patientDTO.getEmail() != null && !patient.getEmail().equals(patientDTO.getEmail()) &&
                patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new DuplicateResourceException("Un patient avec cet email existe déjà");
        }
        if (patientDTO.getNumeroSecuriteSociale() != null && !patient.getNumeroSecuriteSociale().equals(patientDTO.getNumeroSecuriteSociale()) &&
                patientRepository.existsByNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale())) {
            throw new DuplicateResourceException("Un patient avec ce numéro de sécurité sociale existe déjà");
        }

        patient.setNom(patientDTO.getNom());
        patient.setPrenom(patientDTO.getPrenom());
        patient.setEmail(patientDTO.getEmail());
        patient.setTelephone(patientDTO.getTelephone());
        patient.setDateNaissance(patientDTO.getDateNaissance());
        patient.setAdresse(patientDTO.getAdresse());
        patient.setNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale());
        patient.setAntecedents(patientDTO.getAntecedents());
        patient.setAllergies(patientDTO.getAllergies());

        return patientRepository.save(patient);
    }

    public void supprimerPatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient non trouvé avec l'ID: " + id);
        }
        patientRepository.deleteById(id);
    }

    // Méthode interne pour convertir un Patient en PatientDTO
    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getNom(),
                patient.getPrenom(),
                patient.getEmail(),
                patient.getAdresse(),
                patient.getTelephone(),
                patient.getDateNaissance(),
                patient.getNumeroSecuriteSociale(),
                patient.getAntecedents(),
                patient.getAllergies()
        );
    }
}
