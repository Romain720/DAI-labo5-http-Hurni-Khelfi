# Étape 2 : Configuration Docker Compose

Ce document décrit la configuration initiale de Docker Compose pour orchestrer notre serveur web statique.

## Objectif

L'objectif de cette étape est de :
1. Configurer Docker Compose pour gérer notre conteneur Nginx
2. Faciliter le déploiement de l'application
3. Préparer l'infrastructure pour l'ajout futur de services

## Structure du Projet

```
project/
├── docker-compose.yml          # Configuration Docker Compose
└── static-web-server/         # Dossier du serveur web statique
    ├── Dockerfile
    ├── nginx.conf
    └── static-web-server/
        └── ... (fichiers statiques)
```

## Configuration Docker Compose

Le fichier `docker-compose.yml` initial :

```yaml
version: '3.8'                  # Version de Docker Compose

services:
  web:                         # Service pour le serveur web statique
    build:                     # Configuration de build
      context: ./static-web-server
    ports:                     # Mapping des ports
      - "8080:80"             # Port local 8080 vers port conteneur 80
    container_name: static-web-server
```

### Explication de la Configuration

1. **Version** :
   - `version: '3.8'` : Utilise la version 3.8 de Docker Compose qui offre toutes les fonctionnalités modernes.

2. **Services** :
   - `web` : Nom du service pour notre serveur web statique.
   
3. **Build** :
   - `context: ./static-web-server` : Indique le dossier contenant le Dockerfile.
   
4. **Ports** :
   - `"8080:80"` : Redirige le port 8080 de l'hôte vers le port 80 du conteneur.
   
5. **Container Name** :
   - `container_name: static-web-server` : Nom explicite pour le conteneur.

## Commandes Docker Compose

### Démarrage des Services
```bash
# Construire et démarrer les services
docker compose up -d

# Uniquement construire les images
docker compose build

# Construire les images et démarrer les services
docker compose up --build -d
```

### Gestion des Services
```bash
# Arrêter les services
docker compose down

# Voir les logs
docker compose logs

# Voir le statut des services
docker compose ps
```

## Vérification

1. **Accès au Site Web** :
   - URL : `http://localhost:8080`
   - Le site devrait être accessible via un navigateur web

2. **Vérification des Conteneurs** :
   ```bash
   docker compose ps
   ```
   Devrait montrer le conteneur `static-web-server` en cours d'exécution.

## Avantages de Docker Compose

1. **Simplicité** :
   - Configuration déclarative dans un seul fichier
   - Commandes simples pour gérer l'ensemble des services

2. **Reproductibilité** :
   - Environnement identique pour tous les développeurs
   - Configuration versionnée avec le code

3. **Évolutivité** :
   - Facilité d'ajout de nouveaux services
   - Configuration simple des dépendances entre services

## Critères d'Acceptation

✅ Fichier docker-compose.yml créé et configuré  
✅ Service web statique correctement défini  
✅ Ports correctement mappés  
✅ Documentation complète des commandes  
✅ Services accessibles via les ports configurés
