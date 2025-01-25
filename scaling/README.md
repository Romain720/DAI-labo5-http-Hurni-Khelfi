# Étape 5 : Scalabilité et Load Balancing

Ce document décrit la configuration de la scalabilité et du load balancing avec Traefik.

## Configuration

Dans le fichier `docker-compose.yml`, nous ajoutons simplement la section `deploy` pour chaque service :

```yaml
services:
  web:
    # ... autres configurations ...
    deploy:
      replicas: 2  # Nombre d'instances du serveur web statique

  api:
    # ... autres configurations ...
    deploy:
      replicas: 2  # Nombre d'instances de l'API
```

## Gestion des Instances

### Démarrer avec Plusieurs Instances
```bash
docker compose up -d
```
Cette commande démarre 2 instances de chaque service comme spécifié dans le fichier docker-compose.yml.

### Modifier le Nombre d'Instances Dynamiquement
```bash
# Augmenter le nombre d'instances du serveur web à 3
docker compose up -d --scale web=3

# Augmenter le nombre d'instances de l'API à 4
docker compose up -d --scale api=4
```

### Vérifier les Instances en Cours
```bash
docker compose ps
```

## Preuve du Load Balancing en Action

Les logs suivants montrent Traefik distribuant les requêtes entre différentes instances du serveur web (`web-1` et `web-2`) :

```log
web-2  | 172.20.0.4 - - [22/Jan/2025:14:24:44 +0000] "GET / HTTP/1.1" 200 17507 "-" "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0" "172.20.0.1"
web-1  | 172.20.0.4 - - [22/Jan/2025:14:24:44 +0000] "GET /favicon.ico HTTP/1.1" 404 153 "http://localhost/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0" "172.20.0.1"
web-2  | 172.20.0.4 - - [22/Jan/2025:14:24:44 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0" "172.20.0.1"
web-2  | 172.20.0.4 - - [22/Jan/2025:14:24:45 +0000] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0" "172.20.0.1"
```

### Analyse des Logs

1. **Distribution des Requêtes** :
   - On voit que les requêtes sont distribuées entre `web-1` et `web-2`
   - Les timestamps montrent des requêtes successives traitées par différentes instances

2. **Types de Requêtes** :
   - `GET /` : Requête principale de la page
   - `GET /favicon.ico` : Requête pour l'icône du site
   - Les codes de statut (200, 304, 404) montrent différents types de réponses

3. **Round-Robin** :
   - Traefik utilise par défaut un algorithme round-robin
   - Les requêtes sont distribuées de manière équitable entre les instances

## Avantages

1. **Haute Disponibilité**
   - Les services restent disponibles même si une instance tombe en panne
   - Traefik détecte automatiquement les instances défaillantes

2. **Équilibrage de Charge**
   - Distribution automatique du trafic entre les instances
   - Meilleure utilisation des ressources

3. **Scalabilité Dynamique**
   - Ajout/suppression d'instances sans interruption de service
   - Adaptation facile à la charge

## Critères d'Acceptation

✅ Multiple instances configurées dans docker-compose.yml  
✅ Possibilité d'ajouter/supprimer des instances dynamiquement  
✅ Load balancing automatique par Traefik (prouvé par les logs)  
✅ Mise à jour dynamique de l'équilibrage de charge  
✅ Documentation complète de la configuration
