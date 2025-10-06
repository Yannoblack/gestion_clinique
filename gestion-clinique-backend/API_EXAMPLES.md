# Exemples d'utilisation de l'API - Création de Patient

## Endpoint: POST /patients

### Headers requis
```
Content-Type: application/json
```

### Exemple JSON valide pour créer un patient

```json
{
  "nom": "Dupont",
  "prenom": "Jean",
  "numeroSecuriteSociale": "123456789012345",
  "adresse": "123 Rue de la Paix, Paris",
  "telephone": "0123456789",
  "dateNaissance": "1990-01-15",
  "antecedents": "Aucun antécédent notable",
  "allergies": "Pénicilline"
}
```

### Exemple JSON minimal (champs obligatoires seulement)

```json
{
  "nom": "Martin",
  "prenom": "Marie",
  "numeroSecuriteSociale": "987654321098765"
}
```

### Exemple JSON avec champs optionnels vides

```json
{
  "nom": "Bernard",
  "prenom": "Pierre",
  "numeroSecuriteSociale": "111222333444555",
  "adresse": null,
  "telephone": null,
  "dateNaissance": "1985-05-20",
  "antecedents": "",
  "allergies": ""
}
```

## Champs obligatoires vs optionnels

### Champs obligatoires (validation @NotBlank)
- `nom` : String (max 100 caractères)
- `prenom` : String (max 100 caractères)  
- `numeroSecuriteSociale` : String (max 20 caractères)

### Champs optionnels
- `adresse` : String (max 255 caractères) - peut être null ou vide
- `telephone` : String (max 20 caractères) - format: chiffres, +, -, espaces
- `dateNaissance` : Date (format yyyy-MM-dd) - doit être dans le passé ou aujourd'hui
- `antecedents` : String (max 500 caractères) - peut être null ou vide
- `allergies` : String (max 500 caractères) - peut être null ou vide

## Réponse attendue

### Succès (201 Created)
```json
{
  "id": 1,
  "nom": "Dupont",
  "prenom": "Jean", 
  "numeroSecuriteSociale": "123456789012345",
  "adresse": "123 Rue de la Paix, Paris",
  "telephone": "0123456789",
  "dateNaissance": "1990-01-15",
  "antecedents": "Aucun antécédent notable",
  "allergies": "Pénicilline"
}
```

### Erreur de validation (400 Bad Request)
```json
{
  "timestamp": "2024-12-25T10:00:00",
  "status": 400,
  "message": "Erreur de validation",
  "errors": [
    {
      "field": "nom",
      "message": "Le nom du patient ne peut pas être vide."
    }
  ]
}
```

## Notes importantes

1. **Content-Type** : Toujours définir sur `application/json`
2. **Casse** : Respecter exactement la casse des noms de champs (camelCase)
3. **Format de date** : Utiliser le format ISO `yyyy-MM-dd`
4. **ID** : Ne pas inclure le champ `id` dans la requête (généré automatiquement)
5. **Numéro de sécurité sociale** : Doit être unique dans la base de données
