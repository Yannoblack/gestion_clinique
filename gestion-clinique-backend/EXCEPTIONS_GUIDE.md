# Guide de Gestion des Exceptions - API Gestion Clinique

## Vue d'ensemble

Ce guide explique la gestion des exceptions dans l'API de gestion de clinique. Toutes les exceptions sont centralisées et gérées par le `GlobalExceptionHandler` pour assurer des réponses d'erreur cohérentes.

## Types d'Exceptions

### 1. EntityNotFoundException
- **HTTP Status**: 404 NOT FOUND
- **Usage**: Lorsqu'une entité (Patient, Médecin, Rendez-vous, etc.) n'est pas trouvée
- **Exemple**: `new EntityNotFoundException("Patient non trouvé avec l'ID : 123")`

### 2. BusinessException
- **HTTP Status**: 400 BAD REQUEST
- **Usage**: Pour les violations de règles métier
- **Exemple**: `new BusinessException("Le patient doit être majeur pour ce type de consultation")`

### 3. DuplicateResourceException
- **HTTP Status**: 409 CONFLICT
- **Usage**: Lorsqu'une ressource existe déjà (contrainte d'unicité)
- **Exemple**: `new DuplicateResourceException("Un patient avec ce numéro de sécurité sociale existe déjà")`

### 4. ResourceAlreadyExistsException
- **HTTP Status**: 409 CONFLICT
- **Usage**: Lorsqu'on tente de créer une ressource qui existe déjà
- **Exemple**: `new ResourceAlreadyExistsException("Nom d'utilisateur déjà utilisé")`

### 5. AppointmentConflictException
- **HTTP Status**: 409 CONFLICT
- **Usage**: Conflits de rendez-vous (même médecin, même horaire)
- **Exemple**: `new AppointmentConflictException("Un rendez-vous existe déjà pour ce médecin à cette heure")`

### 6. InvalidDateException
- **HTTP Status**: 400 BAD REQUEST
- **Usage**: Dates invalides (rendez-vous dans le passé, etc.)
- **Exemple**: `new InvalidDateException("Impossible de créer un rendez-vous dans le passé")`

### 7. UnauthorizedAccessException
- **HTTP Status**: 403 FORBIDDEN
- **Usage**: Accès non autorisé à une ressource
- **Exemple**: `new UnauthorizedAccessException("Accès non autorisé à cette ressource")`

## Exceptions Spring Framework

### Validation
- **MethodArgumentNotValidException**: Erreurs de validation des DTOs (@Valid)
- **ConstraintViolationException**: Violations de contraintes sur les paramètres

### Authentification/Sécurité
- **AuthenticationException**: Erreurs d'authentification générales
- **BadCredentialsException**: Identifiants invalides
- **AccessDeniedException**: Accès refusé

### Base de données
- **DataIntegrityViolationException**: Violations de contraintes de base de données

### Paramètres
- **MethodArgumentTypeMismatchException**: Format de paramètre invalide (ex: ID non numérique)
- **MissingServletRequestParameterException**: Paramètre requis manquant

## Format des Réponses d'Erreur

Toutes les réponses d'erreur suivent le format suivant :

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "message": "Description de l'erreur",
  "errors": {
    "champ": "Message d'erreur spécifique"
  }
}
```

## Validation des Modèles

### Patient
- `nom`: Obligatoire, 2-50 caractères
- `prenom`: Obligatoire, 2-50 caractères
- `numeroSecuriteSociale`: Obligatoire, exactement 15 chiffres
- `telephone`: Optionnel, exactement 10 chiffres
- `adresse`: Optionnel, max 255 caractères

### Médecin
- `nom`: Obligatoire, 2-50 caractères
- `prenom`: Obligatoire, 2-50 caractères
- `specialite`: Obligatoire, unique, max 100 caractères
- `numeroOrdre`: Optionnel, unique, 8-11 chiffres
- `telephone`: Optionnel, exactement 10 chiffres

### Utilisateur
- `username`: Obligatoire, 3-50 caractères, lettres/chiffres/underscores uniquement
- `password`: Obligatoire, minimum 6 caractères
- `role`: Obligatoire, doit être ROLE_ADMIN, ROLE_USER, ROLE_MEDECIN ou ROLE_SECRETAIRE

### Rendez-vous
- `patient`: Obligatoire, ne peut pas être null
- `medecin`: Obligatoire, ne peut pas être null
- `dateHeure`: Obligatoire, doit être dans le futur
- `salle`: Optionnel, max 50 caractères

## Bonnes Pratiques

1. **Utilisez les exceptions spécifiques** plutôt que des exceptions génériques
2. **Messages d'erreur clairs** et informatifs pour l'utilisateur
3. **Validation côté serveur** avec les annotations Jakarta Validation
4. **Gestion centralisée** via le GlobalExceptionHandler
5. **Codes HTTP appropriés** selon le type d'erreur
6. **Tests unitaires** pour valider la gestion des exceptions

## Exemples d'Utilisation

### Dans un Service
```java
@Service
public class PatientService {
    
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'ID : " + id));
    }
    
    public Patient createPatient(PatientDTO dto) {
        if (patientRepository.existsByNumeroSecuriteSociale(dto.getNumeroSecuriteSociale())) {
            throw new DuplicateResourceException("Un patient avec ce numéro de sécurité sociale existe déjà");
        }
        // ... logique de création
    }
}
```

### Dans un Contrôleur
```java
@RestController
public class PatientController {
    
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody PatientDTO dto) {
        Patient patient = patientService.createPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }
}
```

Les exceptions seront automatiquement capturées et transformées en réponses HTTP appropriées par le `GlobalExceptionHandler`.
