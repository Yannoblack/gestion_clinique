# Test des Exceptions - Diagnostic

## Problème identifié et corrigé

### ❌ **Problème principal :**
Le gestionnaire d'exceptions global ne gérait pas `ResourceNotFoundException`, ce qui causait une erreur 500 au lieu d'une erreur 404 appropriée.

### ✅ **Correction apportée :**
Ajout du gestionnaire pour `ResourceNotFoundException` dans `GlobalExceptionHandler.java` :

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
}
```

## Test des exceptions

### 1. Test avec un patient inexistant
```bash
POST http://localhost:8080/rendezvous
Content-Type: application/json

{
  "patientId": 999,  // ID qui n'existe pas
  "medecinId": 1,
  "dateHeure": "2025-12-28T10:00:00",
  "salle": "Salle A"
}
```

**Résultat attendu :** 404 Not Found avec message clair

### 2. Test avec un médecin inexistant
```bash
POST http://localhost:8080/rendezvous
Content-Type: application/json

{
  "patientId": 1,
  "medecinId": 999,  // ID qui n'existe pas
  "dateHeure": "2025-12-28T10:00:00",
  "salle": "Salle A"
}
```

**Résultat attendu :** 404 Not Found avec message clair

### 3. Test avec des données manquantes
```bash
POST http://localhost:8080/rendezvous
Content-Type: application/json

{
  "patientId": null,
  "medecinId": 1,
  "dateHeure": "2025-12-28T10:00:00",
  "salle": "Salle A"
}
```

**Résultat attendu :** 400 Bad Request avec message de validation

## Exceptions disponibles

### Exceptions personnalisées :
- ✅ `ResourceNotFoundException` - Ressource non trouvée (404)
- ✅ `EntityNotFoundException` - Entité non trouvée (404)
- ✅ `BusinessException` - Erreur métier (400)
- ✅ `DuplicateResourceException` - Ressource dupliquée (409)
- ✅ `ResourceAlreadyExistsException` - Ressource existe déjà (409)
- ✅ `UnauthorizedAccessException` - Accès non autorisé (403)
- ✅ `AppointmentConflictException` - Conflit de rendez-vous (409)
- ✅ `InvalidDateException` - Date invalide (400)

### Gestionnaires Spring :
- ✅ `MethodArgumentNotValidException` - Validation des arguments (400)
- ✅ `ConstraintViolationException` - Violation de contraintes (400)
- ✅ `IllegalArgumentException` - Argument illégal (400)
- ✅ `DataIntegrityViolationException` - Violation d'intégrité (409)
- ✅ `Exception` - Toutes les autres exceptions (500)

## Messages d'erreur améliorés

Les messages d'erreur incluent maintenant :
- Timestamp de l'erreur
- Code de statut HTTP
- Type d'erreur
- Message descriptif
- Chemin de la requête (si disponible)
