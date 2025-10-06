package com.clinique.yannic.gestion_clinique_backend.controllers;

import com.clinique.yannic.gestion_clinique_backend.models.Patient;
import com.clinique.yannic.gestion_clinique_backend.controllers.dto.PatientDTO;
import com.clinique.yannic.gestion_clinique_backend.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Gestion des patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau patient")
    public ResponseEntity<Patient> creerPatient(@Valid @RequestBody PatientDTO patientDTO) {
        Patient patient = patientService.creerPatient(patientDTO);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les patients")
    public ResponseEntity<List<PatientDTO>> obtenirTousLesPatients() {
        List<Patient> patients = patientService.obtenirTousLesPatients();
        // Conversion en DTO
        List<PatientDTO> patientsDTO = patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patientsDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un patient par ID")
    public ResponseEntity<Patient> obtenirPatientParId(@PathVariable Long id) {
        Patient patient = patientService.obtenirPatientParId(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des patients par nom ou prénom")
    public ResponseEntity<List<Patient>> rechercherPatients(@RequestParam String q) {
        List<Patient> patients = patientService.rechercherPatients(q);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un patient")
    public ResponseEntity<Patient> modifierPatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        Patient patientModifie = patientService.modifierPatient(id, patientDTO);
        return ResponseEntity.ok(patientModifie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un patient")
    public ResponseEntity<Void> supprimerPatient(@PathVariable Long id) {
        patientService.supprimerPatient(id);
        return ResponseEntity.noContent().build();
    }

    // Méthode pour convertir un Patient en PatientDTO
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
