-- Script d'initialisation de la base de données clinique_db
-- Exécuter ce script dans PostgreSQL avant de démarrer l'application

-- Créer la base de données si elle n'existe pas
CREATE DATABASE clinique_db;

-- Se connecter à la base de données
\c clinique_db;

-- Créer les extensions nécessaires
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Vérifier la connexion
SELECT 'Base de données clinique_db initialisée avec succès' as message;

-- Afficher les informations de connexion
SELECT 
    current_database() as database_name,
    current_user as user_name,
    version() as postgresql_version;
