-- Script de test pour vérifier la connexion PostgreSQL
-- Utilisez ce script dans pgAdmin ou psql pour tester la connexion

-- 1. Vérifier que la base de données existe
SELECT datname FROM pg_database WHERE datname = 'gestion_clinique';

-- 2. Vérifier les tables existantes
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' AND table_type = 'BASE TABLE';

-- 3. Créer la base de données si elle n'existe pas
-- CREATE DATABASE gestion_clinique;

-- 4. Vérifier les utilisateurs
SELECT usename FROM pg_user WHERE usename = 'postgres';

-- 5. Tester une connexion simple
SELECT current_database(), current_user, version();
