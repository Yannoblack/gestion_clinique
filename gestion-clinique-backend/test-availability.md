# Test de disponibilité des patients et médecins

## Problème identifié
L'erreur "L'ID du patient est obligatoire" indique que le patient avec l'ID 10 n'existe pas dans votre base de données.

## Solutions

### 1. Vérifier les patients disponibles
```bash
GET http://localhost:8080/api/patients
```

### 2. Vérifier les médecins disponibles
```bash
GET http://localhost:8080/api/medecins
```

### 3. Vérifier la disponibilité avant de créer un rendez-vous
```bash
GET http://localhost:8080/rendezvous/check-availability?patientId=10&medecinId=3
```

## Exemple de création de rendez-vous avec des IDs valides

Une fois que vous avez identifié les IDs valides, utilisez-les :

```json
{
  "patientId": 1,
  "medecinId": 1,
  "dateHeure": "2025-12-28T10:00:00",
  "salle": "Salle A",
  "statut": "PLANIFIE",
  "notes": "Consultation de routine"
}
```

## Messages d'erreur améliorés

Les messages d'erreur sont maintenant plus clairs :
- "Patient non trouvé avec l'ID : 10. Veuillez vérifier que ce patient existe dans la base de données."
- "Médecin non trouvé avec l'ID : 3. Veuillez vérifier que ce médecin existe dans la base de données."
