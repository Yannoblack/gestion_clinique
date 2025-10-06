package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.PrescriptionRequestDTO;
import com.clinique.yannic.gestion_clinique_backend.exception.EntityNotFoundException;
import com.clinique.yannic.gestion_clinique_backend.models.Medecin;
import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import com.clinique.yannic.gestion_clinique_backend.models.Prescription;
import com.clinique.yannic.gestion_clinique_backend.models.RendezVous;
import com.clinique.yannic.gestion_clinique_backend.repositories.MedecinRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.PatientRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.PrescriptionRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescription non trouvée avec l'ID : " + id));
    }

    public Prescription createPrescription(Prescription prescription) {
        if (prescription.getRendezVous() == null || prescription.getMedecin() == null) {
            throw new IllegalArgumentException("RendezVous et Medecin doivent être spécifiés pour une Prescription.");
        }
        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(Prescription prescription) {
        if (!prescriptionRepository.existsById(prescription.getId())) {
            throw new EntityNotFoundException("Prescription non trouvée pour la mise à jour.");
        }
        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new EntityNotFoundException("Prescription non trouvée pour la suppression.");
        }
        prescriptionRepository.deleteById(id);
    }

    public List<Prescription> findPrescriptionsByRendezVous(Long rendezVousId) {
        return prescriptionRepository.findByRendezVousId(rendezVousId);
    }

    public List<Prescription> findPrescriptionsByMedecin(Long medecinId) {
        return prescriptionRepository.findByMedecinId(medecinId);
    }

    // Méthode privée pour mapper le DTO vers une entité Prescription (création ou mise à jour)
    private void mapDtoToPrescription(PrescriptionRequestDTO dto, Prescription prescription) {
        Patient patient = patientRepository.findById(dto.getIdPatient())
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'ID : " + dto.getIdPatient()));

        RendezVous rendezVous = rendezVousRepository.findById(dto.getIdRendezVous())
                .orElseThrow(() -> new EntityNotFoundException("RendezVous non trouvé avec l'ID : " + dto.getIdRendezVous()));

        Medecin medecin = medecinRepository.findById(dto.getIdMedecin())
                .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé avec l'ID : " + dto.getIdMedecin()));

        // Attention : si Prescription n'a pas setPatient(), on peut retirer patient ici
        // Ou bien garder si vous avez ajouté ce setter
        prescription.setRendezVous(rendezVous);
        prescription.setMedecin(medecin);
        prescription.setMedicaments(dto.getMedicaments());
        prescription.setInstructions(dto.getInstructions());
        prescription.setAutresInformations(dto.getAutresInformations());
    }

    // Méthode de création à partir du DTO
    public Prescription createPrescriptionFromDTO(PrescriptionRequestDTO dto) {
        Prescription prescription = new Prescription();
        mapDtoToPrescription(dto, prescription);
        return prescriptionRepository.save(prescription);
    }

    // Méthode de mise à jour à partir du DTO
    public Prescription updatePrescriptionFromDTO(Long id, PrescriptionRequestDTO dto) {
        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescription non trouvée avec l'ID : " + id));
        mapDtoToPrescription(dto, existingPrescription);
        return prescriptionRepository.save(existingPrescription);
    }
}
