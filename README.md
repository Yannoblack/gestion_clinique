# 🏥 Système de Gestion Clinique

Un système complet de gestion clinique développé avec Spring Boot (backend) et Angular (frontend), permettant la gestion des patients, médecins, rendez-vous, prescriptions et facturation.

## 📋 Table des matières

- [Vue d'ensemble](#vue-densemble)
- [Architecture](#architecture)
- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [API Documentation](#api-documentation)
- [Structure du projet](#structure-du-projet)
- [Tests](#tests)
- [Déploiement](#déploiement)
- [Contribution](#contribution)

## 🎯 Vue d'ensemble

Ce projet est un système de gestion clinique complet qui permet aux professionnels de santé de gérer efficacement leurs patients, rendez-vous, prescriptions et facturation. Le système est conçu avec une architecture moderne et sécurisée.

### Principales entités métier :
- **Patients** : Gestion des informations personnelles et médicales
- **Médecins** : Gestion des professionnels de santé et spécialités
- **Rendez-vous** : Planification et suivi des consultations
- **Prescriptions** : Gestion des ordonnances médicales
- **Factures** : Système de facturation et suivi des paiements
- **Utilisateurs** : Gestion des accès et rôles

## 🏗️ Architecture

Le projet suit une architecture en couches avec séparation claire entre le frontend et le backend :

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│   Frontend      │◄──────────────►│   Backend       │
│   (Angular)     │                 │   (Spring Boot) │
└─────────────────┘                 └─────────────────┘
         │                                   │
         │                                   │
    ┌─────────┐                         ┌─────────┐
    │ Browser │                         │Database │
    │  Local  │                         │(PostgreSQL)│
    └─────────┘                         └─────────┘
```

## ✨ Fonctionnalités

### 🔐 Authentification et Autorisation
- Système d'authentification JWT
- Gestion des rôles (ADMIN, USER, MEDECIN, SECRETAIRE)
- Sécurité des endpoints avec Spring Security

### 👥 Gestion des Patients
- Création et modification des profils patients
- Gestion des informations médicales (antécédents, allergies)
- Numéro de sécurité sociale unique
- Historique médical complet

### 👨‍⚕️ Gestion des Médecins
- Profils des médecins avec spécialités
- Numéro d'ordre des médecins
- Gestion des disponibilités

### 📅 Gestion des Rendez-vous
- Planification des consultations
- Vérification des conflits d'horaires
- Gestion des salles de consultation
- Statuts des rendez-vous (PLANIFIE, CONFIRME, ANNULE)

### 💊 Gestion des Prescriptions
- Création d'ordonnances médicales
- Suivi des prescriptions
- Liaison avec les rendez-vous

### 💰 Système de Facturation
- Génération automatique de factures
- Gestion des lignes de facturation
- Suivi des paiements
- Statuts des factures (PAYEE, NON_PAYEE)

## 🛠️ Technologies utilisées

### Backend
- **Java 21** - Langage de programmation
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security** - Authentification et autorisation
- **Spring Data JPA** - Persistance des données
- **PostgreSQL** - Base de données principale
- **H2 Database** - Base de données de test
- **JWT (Auth0)** - Tokens d'authentification
- **Swagger/OpenAPI** - Documentation API
- **Maven** - Gestion des dépendances
- **Lombok** - Réduction du code boilerplate

### Frontend
- **Angular 17.3.0** - Framework frontend
- **TypeScript 5.4.2** - Langage de programmation
- **SCSS** - Préprocesseur CSS
- **RxJS** - Programmation réactive
- **Angular CLI** - Outils de développement

### Outils de développement
- **Git** - Contrôle de version
- **Maven** - Build et gestion des dépendances
- **npm** - Gestion des packages Node.js
- **Karma/Jasmine** - Tests unitaires Angular

## 📋 Prérequis

### Pour le Backend
- Java 21 ou supérieur
- Maven 3.6 ou supérieur
- PostgreSQL 12 ou supérieur
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Pour le Frontend
- Node.js 18 ou supérieur
- npm 8 ou supérieur
- Angular CLI 17.3.17

### Base de données
- PostgreSQL avec une base de données nommée `gestion_clinique`
- Utilisateur PostgreSQL avec les droits appropriés

## 🚀 Installation

### 1. Cloner le projet
```bash
git clone <url-du-repo>
cd gestion-clinique
```

### 2. Configuration de la base de données
```sql
-- Créer la base de données
CREATE DATABASE gestion_clinique;

-- Créer un utilisateur (optionnel)
CREATE USER clinique_user WITH PASSWORD 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON DATABASE gestion_clinique TO clinique_user;
```

### 3. Configuration du Backend

#### Modifier les paramètres de connexion
Éditez le fichier `gestion-clinique-backend/src/main/resources/application.properties` :

```properties
# Configuration PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_clinique
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe

# JWT configuration
jwt.secret=votre_secret_jwt_ici
jwt.issuer=gestion_clinique
jwt.expirationMinutes=300
```

#### Compiler et lancer le backend
```bash
cd gestion-clinique-backend
mvn clean install
mvn spring-boot:run
```

Le backend sera accessible sur `http://localhost:8080/swagger-ui/index.html#/`

### 4. Configuration du Frontend

```bash
cd gestioncliniquefrontend
npm install
ng serve
```

Le frontend sera accessible sur `http://localhost:4200`

## ⚙️ Configuration

### Variables d'environnement Backend
- `DB_HOST` : Hôte de la base de données (défaut: localhost)
- `DB_PORT` : Port de la base de données (défaut: 5432)
- `DB_NAME` : Nom de la base de données (défaut: gestion_clinique)
- `DB_USERNAME` : Nom d'utilisateur de la base de données
- `DB_PASSWORD` : Mot de passe de la base de données
- `JWT_SECRET` : Secret pour la signature JWT
- `JWT_EXPIRATION` : Durée d'expiration des tokens (en minutes)

### Configuration CORS
Le backend est configuré pour accepter les requêtes depuis le frontend Angular. Les origines autorisées peuvent être modifiées dans `SecurityConfig.java`.

## 📖 Utilisation

### Accès à l'application
1. Ouvrez votre navigateur sur `http://localhost:4200`
2. Connectez-vous avec vos identifiants
3. Naviguez dans les différentes sections selon vos permissions

### Rôles et permissions
- **ADMIN** : Accès complet à toutes les fonctionnalités
- **MEDECIN** : Gestion des patients, rendez-vous, prescriptions
- **SECRETAIRE** : Gestion des rendez-vous et patients
- **USER** : Accès limité selon les besoins

## 📚 API Documentation

### Swagger UI
Une fois le backend démarré, accédez à la documentation interactive :
- URL : `http://localhost:8080/swagger-ui.html`
- Documentation OpenAPI : `http://localhost:8080/v3/api-docs`

### Endpoints principaux

#### Authentification
- `POST /api/auth/login` - Connexion
- `POST /api/auth/register` - Inscription
- `POST /api/auth/refresh` - Renouvellement de token

#### Patients
- `GET /api/patients` - Liste des patients
- `GET /api/patients/{id}` - Détails d'un patient
- `POST /api/patients` - Créer un patient
- `PUT /api/patients/{id}` - Modifier un patient
- `DELETE /api/patients/{id}` - Supprimer un patient

#### Médecins
- `GET /api/medecins` - Liste des médecins
- `GET /api/medecins/{id}` - Détails d'un médecin
- `POST /api/medecins` - Créer un médecin
- `PUT /api/medecins/{id}` - Modifier un médecin

#### Rendez-vous
- `GET /rendezvous` - Liste des rendez-vous
- `GET /rendezvous/{id}` - Détails d'un rendez-vous
- `POST /rendezvous` - Créer un rendez-vous
- `PUT /rendezvous/{id}` - Modifier un rendez-vous
- `DELETE /rendezvous/{id}` - Annuler un rendez-vous

#### Prescriptions
- `GET /prescriptions` - Liste des prescriptions
- `GET /prescriptions/{id}` - Détails d'une prescription
- `POST /prescriptions` - Créer une prescription

#### Factures
- `GET /factures` - Liste des factures
- `GET /factures/{id}` - Détails d'une facture
- `POST /factures` - Créer une facture
- `PUT /factures/{id}/statut` - Modifier le statut de paiement

## 📁 Structure du projet

```
gestion-clinique/
├── gestion-clinique-backend/          # Backend Spring Boot
│   ├── src/main/java/com/clinique/yannic/gestion_clinique_backend/
│   │   ├── Config/                    # Configuration (Swagger, Jackson)
│   │   ├── controllers/               # Contrôleurs REST
│   │   ├── exception/                 # Gestion des exceptions
│   │   ├── models/                    # Entités JPA
│   │   ├── repositories/              # Repositories Spring Data
│   │   ├── security/                  # Configuration sécurité
│   │   └── services/                  # Logique métier
│   ├── src/main/resources/
│   │   └── application.properties     # Configuration
│   └── pom.xml                       # Dépendances Maven
├── gestioncliniquefrontend/           # Frontend Angular
│   ├── src/app/
│   │   ├── core/                     # Services core
│   │   ├── pages/                    # Composants de pages
│   │   └── services/                 # Services Angular
│   ├── src/assets/                   # Ressources statiques
│   └── package.json                  # Dépendances npm
└── README.md                         # Ce fichier
```

## 🧪 Tests

### Tests Backend
```bash
cd gestion-clinique-backend
mvn test
```

### Tests Frontend
```bash
cd gestioncliniquefrontend
ng test
```

### Tests d'intégration
Les tests d'intégration sont disponibles dans le package `integration` du backend.

## 🚀 Déploiement

### Backend
1. Compiler l'application : `mvn clean package`
2. Déployer le JAR généré sur votre serveur
3. Configurer les variables d'environnement
4. Démarrer l'application : `java -jar gestion-clinique-backend.jar`

### Frontend
1. Compiler l'application : `ng build --prod`
2. Déployer le contenu du dossier `dist/` sur votre serveur web
3. Configurer le serveur pour pointer vers l'API backend

### Docker (optionnel)
Des fichiers Dockerfile peuvent être ajoutés pour containeriser l'application.

## 🤝 Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commit vos changements (`git commit -am 'Ajout d'une nouvelle fonctionnalité'`)
4. Push vers la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Créer une Pull Request

### Standards de code
- Respecter les conventions Java (Backend)
- Respecter les conventions TypeScript/Angular (Frontend)
- Ajouter des tests pour les nouvelles fonctionnalités
- Documenter les nouvelles APIs

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 📞 Support

Pour toute question ou problème :
- Créer une issue sur le repository
- Consulter la documentation API sur Swagger UI
- Vérifier les logs d'application pour le debugging

## 🔄 Changelog

### Version 1.0.0
- Implémentation initiale du système
- Gestion des patients, médecins, rendez-vous
- Système d'authentification JWT
- Interface Angular de base
- Documentation API complète

---

**Développé avec ❤️ pour améliorer la gestion des cliniques**
