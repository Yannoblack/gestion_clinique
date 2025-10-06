package com.clinique.yannic.gestion_clinique_backend.controllers;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.PrescriptionRequestDTO;
import com.clinique.yannic.gestion_clinique_backend.exception.EntityNotFoundException;
import com.clinique.yannic.gestion_clinique_backend.models.Prescription;
import com.clinique.yannic.gestion_clinique_backend.services.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Operation(summary = "Récupérer toutes les prescriptions", description = "Renvoie la liste de toutes les prescriptions.")
    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @Operation(summary = "Récupérer une prescription par ID", description = "Renvoie une prescription spécifique en fonction de son ID.")
    @Parameter(name = "id", description = "ID de la prescription à récupérer", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        Optional<Prescription> optionalPrescription = Optional.ofNullable(prescriptionService.getPrescriptionById(id));
        return optionalPrescription.map(prescription -> new ResponseEntity<>(prescription, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Créer une nouvelle prescription", 
            description = "Crée une nouvelle prescription.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrescriptionRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Prescription Example",
                                    summary = "Exemple de création d'une prescription",
                                    value = """
                                            {
                                              "idPatient": 1,
                                              "idRendezVous": 1,
                                              "idMedecin": 1,
                                              "medicaments": "Paracétamol 500mg",
                                              "instructions": "1 comprimé 3 fois par jour",
                                              "autresInformations": "À prendre après les repas"
                                            }
                                            """
                            )
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody PrescriptionRequestDTO prescriptionRequestDTO) {
        try {
            Prescription createdPrescription = prescriptionService.createPrescriptionFromDTO(prescriptionRequestDTO);
            return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Mettre à jour une prescription", description = "Met à jour une prescription existante.")
    @Parameter(name = "id", description = "ID de la prescription à mettre à jour", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails de la prescription à mettre à jour", required = true, content = @Content(schema = @Schema(implementation = PrescriptionRequestDTO.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionRequestDTO prescriptionRequestDTO) {
        try {
            Prescription updatedPrescription = prescriptionService.updatePrescriptionFromDTO(id, prescriptionRequestDTO);
            return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Supprimer une prescription", description = "Supprime une prescription en fonction de son ID.")
    @Parameter(name = "id", description = "ID de la prescription à supprimer", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        try {
            prescriptionService.deletePrescription(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Récupérer les prescriptions par rendez-vous", description = "Renvoie les prescriptions associées à un rendez-vous spécifique.")
    @Parameter(name = "idRendezVous", description = "ID du rendez-vous pour récupérer les prescriptions", required = true)
    @GetMapping("/rendezVous/{idRendezVous}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByRendezVous(@PathVariable Long idRendezVous) {
        try {
            List<Prescription> prescriptions = prescriptionService.findPrescriptionsByRendezVous(idRendezVous);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Récupérer les prescriptions par médecin", description = "Renvoie les prescriptions associées à un médecin spécifique.")
    @Parameter(name = "idMedecin", description = "ID du médecin pour récupérer les prescriptions", required = true)
    @GetMapping("/medecin/{idMedecin}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByMedecin(@PathVariable Long idMedecin) {
        try {
            List<Prescription> prescriptions = prescriptionService.findPrescriptionsByMedecin(idMedecin);
            return new ResponseEntity<>(prescriptions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
