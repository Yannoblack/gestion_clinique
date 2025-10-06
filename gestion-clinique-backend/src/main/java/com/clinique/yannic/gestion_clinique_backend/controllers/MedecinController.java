package com.clinique.yannic.gestion_clinique_backend.controllers;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.MedecinDTO;
import com.clinique.yannic.gestion_clinique_backend.models.Medecin;
import com.clinique.yannic.gestion_clinique_backend.services.MedecinService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/medecins")
@Validated
public class MedecinController {

    private final MedecinService medecinService;

    public MedecinController(MedecinService medecinService) {
        this.medecinService = medecinService;
    }

    /**
     * Récupère la liste de tous les médecins.
     */
    @GetMapping
    public ResponseEntity<List<Medecin>> getAllMedecins() {
        List<Medecin> medecins = medecinService.getAllMedecin();
        return ResponseEntity.ok(medecins);
    }

    /**
     * Récupère un médecin par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Medecin> getMedecinById(@PathVariable @NotNull Long id) {
        Medecin medecin = medecinService.getMedecinById(id);
        return ResponseEntity.ok(medecin);
    }

    /**
     * Crée un nouveau médecin.
     */
    @PostMapping
    public ResponseEntity<Medecin> createMedecin(@Valid @RequestBody MedecinDTO medecinDTO) {
        Medecin createdMedecin = medecinService.createMedecinFromDTO(medecinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedecin);
    }

    /**
     * Met à jour un médecin existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Medecin> updateMedecin(@PathVariable @NotNull Long id, @Valid @RequestBody MedecinDTO medecinDTO) {
        // Récupère le médecin existant
        Medecin existingMedecin = medecinService.getMedecinById(id);
        // Met à jour ses attributs avec ceux du DTO
        existingMedecin.setNom(medecinDTO.getNom());
        existingMedecin.setPrenom(medecinDTO.getPrenom());
        existingMedecin.setSpecialite(medecinDTO.getSpecialite());
        existingMedecin.setNumeroOrdre(medecinDTO.getNumeroOrdre());
        existingMedecin.setTelephone(medecinDTO.getTelephone());

        // Sauvegarde et retourne le médecin mis à jour
        Medecin updatedMedecin = medecinService.updateMedecin(existingMedecin);
        return ResponseEntity.ok(updatedMedecin);
    }

    /**
     * Supprime un médecin par son ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable @NotNull Long id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }
}
