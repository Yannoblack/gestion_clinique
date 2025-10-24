package com.clinique.yannic.gestion_clinique_backend.controllers;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.RendezVousDTO;
import com.clinique.yannic.gestion_clinique_backend.controllers.dto.FactureDTO;
import com.clinique.yannic.gestion_clinique_backend.services.RendezVousService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rendezvous")
@Tag(name = "Rendez-vous", description = "API pour la gestion des rendez-vous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    // =========================
    // ENDPOINTS DE LECTURE
    // =========================

    @Operation(
            summary = "Récupérer tous les rendez-vous",
            description = "Récupère la liste de tous les rendez-vous de la clinique"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public ResponseEntity<List<RendezVousDTO>> getAllRendezVous() {
        List<RendezVousDTO> rendezVous = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(rendezVous);
    }

    @Operation(
            summary = "Récupérer un rendez-vous par ID",
            description = "Récupère les détails d'un rendez-vous spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous trouvé"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousDTO> getRendezVousById(
            @Parameter(description = "ID du rendez-vous", required = true, example = "1")
            @PathVariable Long id) {
        RendezVousDTO rendezVous = rendezVousService.getRendezVousById(id);
        return ResponseEntity.ok(rendezVous);
    }

    @Operation(
            summary = "Récupérer les rendez-vous d'un patient",
            description = "Récupère tous les rendez-vous d'un patient spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous du patient récupérés"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByPatientId(
            @Parameter(description = "ID du patient", required = true, example = "1")
            @PathVariable Long patientId) {
        List<RendezVousDTO> rendezVous = rendezVousService.getRendezVousByPatientId(patientId);
        return ResponseEntity.ok(rendezVous);
    }

    @Operation(
            summary = "Récupérer les rendez-vous d'un médecin",
            description = "Récupère tous les rendez-vous d'un médecin spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous du médecin récupérés"),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<RendezVousDTO>> getRendezVousByMedecinId(
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @PathVariable Long medecinId) {
        List<RendezVousDTO> rendezVous = rendezVousService.getRendezVousByMedecinId(medecinId);
        return ResponseEntity.ok(rendezVous);
    }

    @Operation(
            summary = "Récupérer les factures d'un patient",
            description = "Récupère toutes les factures liées aux rendez-vous d'un patient"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factures du patient récupérées"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/patient/{patientId}/factures")
    public ResponseEntity<List<FactureDTO>> getFacturesByPatientId(
            @Parameter(description = "ID du patient", required = true, example = "1")
            @PathVariable Long patientId) {
        List<FactureDTO> factures = rendezVousService.getFacturesByPatientId(patientId);
        return ResponseEntity.ok(factures);
    }

    @Operation(
            summary = "Vérifier la disponibilité d'un patient et d'un médecin",
            description = "Vérifie si un patient et un médecin existent avant de créer un rendez-vous"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient et médecin trouvés"),
            @ApiResponse(responseCode = "404", description = "Patient ou médecin non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @Parameter(description = "ID du patient", required = true, example = "1")
            @RequestParam Long patientId,
            @Parameter(description = "ID du médecin", required = true, example = "1")
            @RequestParam Long medecinId) {
        Map<String, Object> result = rendezVousService.checkPatientAndMedecinAvailability(patientId, medecinId);
        return ResponseEntity.ok(result);
    }

    // =========================
    // ENDPOINTS DE CRÉATION
    // =========================


    @Operation(
            summary = "Créer un nouveau rendez-vous",
            description = "Crée un nouveau rendez-vous avec validation des conflits",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RendezVousDTO.class),
                            examples = @ExampleObject(
                                    name = "Rendez-vous Example",
                                    summary = "Exemple de création d'un rendez-vous",
                                    value = """
                                            {
                                              "patientId": 1,
                                              "medecinId": 1,
                                              "dateHeure": "2025-12-25T10:00:00",
                                              "salle": "Salle A",
                                              "statut": "PLANIFIE",
                                              "notes": "Consultation de routine"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rendez-vous créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou conflit de rendez-vous"),
            @ApiResponse(responseCode = "404", description = "Patient ou médecin non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping
    public ResponseEntity<RendezVousDTO> createRendezVous(
            @Valid @RequestBody RendezVousDTO rendezVousDTO) {
        
        RendezVousDTO createdRendezVous = rendezVousService.createRendezVous(rendezVousDTO);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRendezVous.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(createdRendezVous);
    }

    // =========================
    // ENDPOINTS DE MISE À JOUR
    // =========================

    @Operation(
            summary = "Mettre à jour un rendez-vous",
            description = "Met à jour les informations d'un rendez-vous existant"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou conflit de rendez-vous"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous, patient ou médecin non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDTO> updateRendezVous(
            @Parameter(description = "ID du rendez-vous", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RendezVousDTO rendezVousDTO) {
        
        RendezVousDTO updatedRendezVous = rendezVousService.updateRendezVous(id, rendezVousDTO);
        return ResponseEntity.ok(updatedRendezVous);
    }

    // =========================
    // ENDPOINTS DE SUPPRESSION
    // =========================

    @Operation(
            summary = "Supprimer un rendez-vous",
            description = "Supprime un rendez-vous de la base de données"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rendez-vous supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(
            @Parameter(description = "ID du rendez-vous", required = true, example = "1")
            @PathVariable Long id) {
        
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // GESTION DES ERREURS
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur interne du serveur: " + e.getMessage());
    }
}