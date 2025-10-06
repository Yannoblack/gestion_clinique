package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.FactureDTO;
import com.clinique.yannic.gestion_clinique_backend.exception.EntityNotFoundException;
import com.clinique.yannic.gestion_clinique_backend.models.Facture;
import com.clinique.yannic.gestion_clinique_backend.models.RendezVous;
import com.clinique.yannic.gestion_clinique_backend.repositories.FactureRepository;
import com.clinique.yannic.gestion_clinique_backend.repositories.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final RendezVousRepository rendezVousRepository;

    // Constructeur d'injection
    public FactureService(FactureRepository factureRepository, RendezVousRepository rendezVousRepository) {
        this.factureRepository = factureRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    /**
     * Conversion d'une entité Facture vers un DTO.
     */
    public FactureDTO toDTO(Facture facture) {
        Long rendezVousId = (facture.getRendezVous() != null) ? facture.getRendezVous().getId() : null;
        return new FactureDTO(
                facture.getId(),
                facture.getMontant(),
                facture.getDateFacture(),
                facture.getStatut(),
                rendezVousId
        );
    }

    /**
     * Conversion d'un DTO vers une entité Facture (sans gérer la relation RendezVous).
     */
    public Facture toEntity(FactureDTO dto) {
        Facture facture = new Facture();
        facture.setDateFacture(dto.getDateFacture());
        facture.setMontant(dto.getMontant());
        facture.setStatut(dto.getStatut());
        return facture;
    }

    /**
     * Récupère toutes les factures sous forme de DTO.
     */
    public List<FactureDTO> getAllFactures() {
        return factureRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une facture par ID sous forme de DTO.
     */
    public FactureDTO getFactureDTOById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facture non trouvée avec l'ID : " + id));
        return toDTO(facture);
    }

    /**
     * Supprime une facture par ID.
     */
    @Transactional
    public void deleteFacture(Long id) {
        if (!factureRepository.existsById(id)) {
            throw new EntityNotFoundException("Facture non trouvée pour la suppression avec l'ID : " + id);
        }
        factureRepository.deleteById(id);
    }

    /**
     * Lie une facture existante à un rendez-vous.
     */
    @Transactional
    public FactureDTO linkToRendezVous(Long factureId, Long rendezVousId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new EntityNotFoundException("Facture non trouvée avec l'ID : " + factureId));
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + rendezVousId));

        facture.setRendezVous(rendezVous);
        Facture saved = factureRepository.save(facture);
        return toDTO(saved);
    }

    /**
     * Crée une nouvelle facture à partir d'un DTO.
     */
    @Transactional
    public FactureDTO createFactureFromDTO(FactureDTO dto) {
        Assert.notNull(dto, "Les détails de la facture (DTO) ne peuvent pas être nuls.");

        Facture facture = toEntity(dto);

        // Lier le rendez-vous si un ID est fourni
        if (dto.getRendezVousId() != null) {
            RendezVous rendezVous = rendezVousRepository.findById(dto.getRendezVousId())
                    .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + dto.getRendezVousId()));
            facture.setRendezVous(rendezVous);
        }

        Facture saved = factureRepository.save(facture);
        return toDTO(saved);
    }

    /**
     * Met à jour une facture existante à partir d'un DTO.
     */
    @Transactional
    public FactureDTO updateFactureFromDTO(Long id, FactureDTO dto) {
        Assert.notNull(dto, "Les détails de la facture (DTO) ne peuvent pas être nuls.");

        Facture existingFacture = factureRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Facture non trouvée avec l'ID : " + id));

        existingFacture.setMontant(dto.getMontant());
        existingFacture.setDateFacture(dto.getDateFacture());
        existingFacture.setStatut(dto.getStatut());

        if (dto.getRendezVousId() != null) {
            RendezVous rendezVous = rendezVousRepository.findById(dto.getRendezVousId())
                    .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + dto.getRendezVousId()));
            existingFacture.setRendezVous(rendezVous);
        } else {
            existingFacture.setRendezVous(null);
        }

        Facture saved = factureRepository.save(existingFacture);
        return toDTO(saved);
    }
}
