package com.clinique.yannic.gestion_clinique_backend.controllers;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.FactureDTO;
import com.clinique.yannic.gestion_clinique_backend.services.FactureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/factures")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @Operation(summary = "Récupérer toutes les factures", description = "Retourne la liste de toutes les factures.")
    @ApiResponse(responseCode = "200", description = "Liste des factures retournée avec succès")
    @GetMapping
    public ResponseEntity<List<FactureDTO>> getAllFactures() {
        List<FactureDTO> dtos = factureService.getAllFactures();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Récupérer une facture par son ID", description = "Retourne une facture spécifique identifiée par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facture trouvée et retournée"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FactureDTO> getFactureById(
            @Parameter(description = "ID de la facture à récupérer", required = true)
            @PathVariable Long id) {
        FactureDTO dto = factureService.getFactureDTOById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Créer une nouvelle facture", 
            description = "Crée une facture à partir des données fournies.",
            requestBody = @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FactureDTO.class),
                            examples = @ExampleObject(
                                    name = "Facture Example",
                                    summary = "Exemple de création d'une facture",
                                    value = """
                                            {
                                              "montant": 150.00,
                                              "dateFacture": "2024-12-25",
                                              "statut": "NON_PAYÉE",
                                              "rendezVousId": 1
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facture créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies", content = @Content)
    })
    @PostMapping
    public ResponseEntity<FactureDTO> createFacture(
            @Parameter(description = "Données de la facture à créer", required = true)
            @RequestBody FactureDTO factureDTO) {
        FactureDTO created = factureService.createFactureFromDTO(factureDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Mettre à jour une facture existante", description = "Met à jour la facture identifiée par son ID avec les nouvelles données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facture mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<FactureDTO> updateFacture(
            @Parameter(description = "ID de la facture à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de la facture", required = true)
            @RequestBody FactureDTO factureDTO) {
        FactureDTO updated = factureService.updateFactureFromDTO(id, factureDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer une facture", description = "Supprime la facture identifiée par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Facture supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(
            @Parameter(description = "ID de la facture à supprimer", required = true)
            @PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lier une facture à un rendez-vous", description = "Associe une facture existante à un rendez-vous existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liaison réussie, la facture mise à jour est retournée"),
            @ApiResponse(responseCode = "404", description = "Facture ou Rendez-vous non trouvé", content = @Content)
    })
    @PutMapping("/{factureId}/rendezvous/{rendezVousId}")
    public ResponseEntity<FactureDTO> linkFactureToRendezVous(
            @Parameter(description = "ID de la facture à lier", required = true) @PathVariable Long factureId,
            @Parameter(description = "ID du rendez-vous à associer", required = true) @PathVariable Long rendezVousId) {

        FactureDTO updatedFacture = factureService.linkToRendezVous(factureId, rendezVousId);
        return ResponseEntity.ok(updatedFacture);
    }
}
