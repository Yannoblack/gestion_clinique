package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.MedecinDTO;
import com.clinique.yannic.gestion_clinique_backend.models.Medecin;
import com.clinique.yannic.gestion_clinique_backend.repositories.MedecinRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class MedecinService {

    private final MedecinRepository medecinsRepository;

    public MedecinService(MedecinRepository medecinsRepository) {
        this.medecinsRepository = medecinsRepository;
    }

    @Transactional(readOnly = true)
    public List<Medecin> getAllMedecin() {
        return medecinsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Medecin getMedecinById(Long id) {
        return medecinsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID : " + id));
    }

    /**
     * Récupère un médecin avec ses rendez-vous chargés explicitement.
     */
    @Transactional
    public Medecin getMedecinWithRendezVous(Long id) {
        Medecin medecin = getMedecinById(id);
        Hibernate.initialize(medecin.getRendezVousList());
        return medecin;
    }

    /**
     * Crée un nouveau médecin à partir d'un DTO.
     */
    @Transactional
    public Medecin createMedecinFromDTO(MedecinDTO medecinDTO) {
        Assert.notNull(medecinDTO, "Les détails du médecin (DTO) ne peuvent pas être nuls.");
        Medecin medecin = new Medecin();
        medecin.setNom(medecinDTO.getNom());
        medecin.setPrenom(medecinDTO.getPrenom());
        medecin.setSpecialite(medecinDTO.getSpecialite());
        medecin.setNumeroOrdre(medecinDTO.getNumeroOrdre());
        medecin.setTelephone(medecinDTO.getTelephone());

        return medecinsRepository.save(medecin);
    }

    /**
     * Met à jour un médecin existant.
     */
    @Transactional
    public Medecin updateMedecin(Medecin medecin) {
        return medecinsRepository.save(medecin);
    }

    /**
     * Supprime un médecin par son ID.
     */
    @Transactional
    public void deleteMedecin(Long id) {
        medecinsRepository.deleteById(id);
    }
}
