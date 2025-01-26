# DAI labo 5 http, rapport

# Étape 1 : Site Web Statique

## Objectif

L'objectif de cette étape est de créer une image Docker qui contient un serveur HTTP Nginx servant un site Web statique. Ce site Web statique est une simple page HTML que nous avons créée en utilisant un modèle gratuit.

## Procédure suivie

### 1. Créer le dossier pour le site Web statique

Nous avons créé un dossier `static-web` pour contenir les fichiers de notre site Web statique. À l'intérieur de ce dossier, nous avons ajouté un fichier `index.html` qui constitue la page d'accueil de notre site. Le contenu du fichier HTML a été basé sur un modèle gratuit trouvé sur [Start Bootstrap](https://startbootstrap.com/).

### 2. Création du fichier Dockerfile

Le fichier `Dockerfile` permet de construire une image Docker avec Nginx pour diffuser le contenu statique. Voici le contenu du `Dockerfile` :

```Dockerfile
# Utiliser l'image Nginx de base
FROM nginx:latest

# Copier le contenu statique du site dans le dossier Nginx
COPY ./public /usr/share/nginx/html

# Exposer le port 80 pour la diffusion du site
EXPOSE 80

```

### 3. Configuration de Nginx

Le fichier `nginx.conf` configure le comportement du serveur Nginx :

```nginx
server {
    listen 80;              # Écoute sur le port 80 (HTTP)
    server_name localhost;  # Nom du serveur

    location / {
        root /usr/share/nginx/html;  # Dossier racine contenant les fichiers statiques
        index index.html;            # Fichier par défaut à servir
    }
}
```

Cette configuration :
- Configure le serveur pour écouter sur le port 80 (HTTP standard)
- Définit le nom du serveur comme "localhost"
- Spécifie que tous les fichiers statiques seront servis depuis le dossier `/usr/share/nginx/html`
- Utilise `index.html` comme page par défaut

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

services:
  web:                         # Service pour le serveur web statique
    build:                     # Configuration de build
      context: ./static-web-server
    ports:                     # Mapping des ports
      - "8080:80"             # Port local 8080 vers port conteneur 80
    container_name: static-web-server
```

### Explication de la Configuration

1. **Services** :
   - `web` : Nom du service pour notre serveur web statique.
   
2. **Build** :
   - `context: ./static-web-server` : Indique le dossier contenant le Dockerfile.
   
3. **Ports** :
   - `"8080:80"` : Redirige le port 8080 de l'hôte vers le port 80 du conteneur.
   
4. **Container Name** :
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




# Étape 3 : API REST avec Javalin

Ce document décrit l'implémentation de l'API REST utilisant Javalin pour la gestion des todos.

## Structure du Projet

```
labo5-http-code-java/
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   ├── Main.java          # Configuration Javalin et endpoints
│                   ├── Todo.java          # Modèle de données
│                   └── TodoService.java   # Logique métier
├── Dockerfile                            # Configuration Docker
└── pom.xml                              # Configuration Maven
```

## Configuration Maven

Le fichier `pom.xml` définit les dépendances nécessaires :

```xml
<dependencies>
    <!-- Javalin - Framework Web -->
    <dependency>
        <groupId>io.javalin</groupId>
        <artifactId>javalin</artifactId>
        <version>5.0.1</version>
    </dependency>
    
    <!-- SLF4J - Logging -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.36</version>
    </dependency>
    
    <!-- Jackson - JSON Serialization -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
</dependencies>
```

## Modèle de Données

Le fichier `Todo.java` définit la structure d'un todo :

```java
public class Todo {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    // Constructeurs, getters et setters
}
```

## API REST Endpoints

L'API expose les endpoints suivants dans `Main.java` :

| Méthode | Endpoint     | Description                    | Corps de la Requête              |
|---------|-------------|--------------------------------|----------------------------------|
| GET     | /api        | Récupérer tous les todos       | -                                |
| GET     | /api/{id}   | Récupérer un todo spécifique   | -                                |
| POST    | /api        | Créer un nouveau todo          | `{"title": "", "description": ""}`|
| PUT     | /api/{id}   | Mettre à jour un todo existant | `{"title": "", "description": ""}`|
| DELETE  | /api/{id}   | Supprimer un todo              | -                                |

### Configuration Javalin

```java
public static void main(String[] args) {
    var app = Javalin.create(config -> {
        config.plugins.enableCors(cors -> cors.add(it -> {
            it.allowHost("localhost:8080");
            it.allowHost("localhost:80");
        }));
    }).start(8081);

    // Configuration des routes...
}
```

## Configuration Docker

Le `Dockerfile` pour l'API :

```dockerfile
# Étape de build
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn package

# Étape d'exécution
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/labo5-http-code-java-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
```

## Tests des Endpoints

### 1. Créer un Todo
```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{"title":"Nouvelle tâche","description":"Description","completed":false}' \
     http://localhost:8081/api
```

### 2. Récupérer Tous les Todos
```bash
curl http://localhost:8081/api
```

### 3. Récupérer un Todo Spécifique
```bash
curl http://localhost:8081/api/1
```

### 4. Mettre à Jour un Todo
```bash
curl -X PUT -H "Content-Type: application/json" \
     -d '{"title":"Tâche modifiée","description":"Nouvelle description","completed":true}' \
     http://localhost:8081/api/1
```

### 5. Supprimer un Todo
```bash
curl -X DELETE http://localhost:8081/api/1
```

## Format des Données

### Requête (POST/PUT)
```json
{
    "title": "Titre du todo",
    "description": "Description du todo",
    "completed": false
}
```

### Réponse
```json
{
    "id": 1,
    "title": "Titre du todo",
    "description": "Description du todo",
    "completed": false
}
```

## Gestion des Erreurs

L'API gère les erreurs suivantes :
- 404 Not Found : Todo non trouvé
- 400 Bad Request : Requête invalide
- 500 Internal Server Error : Erreur serveur

## Déploiement

1. **Build de l'image** :
   ```bash
   docker build -t todo-api .
   ```

2. **Lancement du conteneur** :
   ```bash
   docker run -d -p 8081:8081 todo-api
   ```

## Critères d'Acceptation

✅ API REST implémentée avec Javalin  
✅ Tous les endpoints CRUD fonctionnels  
✅ Format JSON pour les requêtes/réponses  
✅ Gestion des erreurs  
✅ Tests des endpoints documentés  
✅ Configuration Docker complète  
✅ Documentation détaillée de l'API


# Étape 4 : Reverse Proxy avec Traefik

Ce document décrit la mise en place d'un reverse proxy Traefik pour gérer le routage entre le serveur web statique et l'API REST.

## Objectifs

1. Mettre en place un reverse proxy avec Traefik
2. Configurer le routage vers les différents services
3. Activer le dashboard Traefik pour la surveillance
4. Sécuriser l'infrastructure

## Architecture

```
                   ┌─────────────────┐
                   │                 │
                   │    Traefik      │
                   │  Reverse Proxy  │
                   │    (Port 80)    │
                   │                 │
                   └────────┬────────┘
                           │
                ┌─────────┴──────────┐
                │                    │
        ┌───────┴─────┐      ┌──────┴──────┐
        │             │      │             │
  ┌─────┴─────┐ ┌────┴────┐ │  ┌────────┐ │
  │  Static   │ │   API   │ │  │Traefik │ │
  │   Web     │ │ Service │ │  │Dashboard│ │
  │  Server   │ │(Port 81)│ │  │(8080)  │ │
  └───────────┘ └─────────┘ │  └────────┘ │
                            └──────────────┘
```

## Configuration Traefik

### docker-compose.yml

```yaml
version: '3.8'

services:
  reverse_proxy:
    image: traefik:v2.10
    command:
      - "--api.insecure=true"           # Active le dashboard
      - "--providers.docker=true"        # Active l'intégration Docker
      - "--providers.docker.exposedbydefault=false"
      - "--entrypoints.web.address=:80"  # Point d'entrée web
    ports:
      - "80:80"      # Port pour le trafic web
      - "8080:8080"  # Port pour le dashboard
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock:ro

  web:
    # ... configuration du service web ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.web.rule=PathPrefix(`/`)"
      - "traefik.http.services.web.loadbalancer.server.port=80"

  api:
    # ... configuration du service api ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.api.rule=PathPrefix(`/api`)"
      - "traefik.http.services.api.loadbalancer.server.port=8081"
```

## Explication de la Configuration

### 1. Service Traefik

#### Commandes
- `--api.insecure=true` : Active le dashboard Traefik (mode non sécurisé pour le développement)
- `--providers.docker=true` : Permet à Traefik de découvrir les services via Docker
- `--providers.docker.exposedbydefault=false` : Désactive l'exposition automatique des services
- `--entrypoints.web.address=:80` : Définit le point d'entrée web sur le port 80

#### Ports
- `80:80` : Port pour le trafic web
- `8080:8080` : Port pour accéder au dashboard Traefik

#### Volumes
- `/var/run/docker.sock:/var/run/docker.sock:ro` : Accès en lecture seule au socket Docker

### 2. Configuration des Services

#### Service Web Statique
```yaml
labels:
  - "traefik.enable=true"  # Active Traefik pour ce service
  - "traefik.http.routers.web.rule=PathPrefix(`/`)"  # Route le trafic racine
  - "traefik.http.services.web.loadbalancer.server.port=80"  # Port du service
```

#### Service API
```yaml
labels:
  - "traefik.enable=true"  # Active Traefik pour ce service
  - "traefik.http.routers.api.rule=PathPrefix(`/api`)"  # Route le trafic /api
  - "traefik.http.services.api.loadbalancer.server.port=8081"  # Port du service
```

## Points d'Accès

- Site Web Statique : `http://localhost/`
- API REST : `http://localhost/api`
- Dashboard Traefik : `http://localhost:8080`

## Avantages du Reverse Proxy

1. **Sécurité**
   - Isolation des services backend
   - Point d'entrée unique
   - Masquage de l'infrastructure interne

2. **Gestion**
   - Configuration centralisée
   - Surveillance via dashboard
   - Facilité de maintenance

3. **Flexibilité**
   - Routage dynamique
   - Découverte automatique des services
   - Scalabilité simplifiée

## Tests de Fonctionnement

1. **Vérifier le Dashboard**
   ```bash
   curl http://localhost:8080
   ```

2. **Tester le Site Web**
   ```bash
   curl http://localhost/
   ```

3. **Tester l'API**
   ```bash
   curl http://localhost/api
   ```

## Déploiement

1. **Construire les images Docker** :
   ```bash
   docker-compose build 
   ```

2. **Lancement du conteneur** :
   ```bash
   docker compose up -d
   ```

## Accéder au Dashboard Traefik
- Dashboard Traefik : `http://localhost:8080`

## Critères d'Acceptation

✅ Traefik configuré et fonctionnel  
✅ Dashboard accessible  
✅ Routage correct vers le site web statique  
✅ Routage correct vers l'API  
✅ Documentation complète  
✅ Tests de fonctionnement validés  
✅ Configuration Docker complète  
✅ Documentation détaillée de l'API


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

### Supprimer dynamiquement des Instances
Il suffis de faire comment l'augmentation de nombre d'instances en cours de service mais avec un nombre plus petit que le nombre d'instances actuellement en cours de service.
#### Exemple : Supprimer 2 instances actuellement en cours de service 
(on part du principe que 4 instances sont actuellement en cours de service)
```bash
# Supprimer 2 instances actuellement en cours de service
docker compose up -d --scale api=2
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


# Étape 6 : Sessions Collantes avec Traefik

Ce document décrit la configuration et la validation des sessions collantes (sticky sessions) pour l'API, tout en maintenant le round-robin pour le serveur web statique.

## Configuration

Dans le fichier `docker-compose.yml`, ajoutez les labels suivants pour le service API :

```yaml
services:
  api:
    # ... autres configurations ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.api.rule=PathPrefix(`/api`)"
      - "traefik.http.services.api.loadbalancer.server.port=8081"
      - "traefik.http.services.api.loadbalancer.sticky=true"
      - "traefik.http.services.api.loadbalancer.sticky.cookie=true"
      - "traefik.http.services.api.loadbalancer.sticky.cookie.name=api_sticky"
      - "traefik.http.services.api.loadbalancer.sticky.cookie.secure=false"
```

Le service web statique garde sa configuration d'origine (round-robin par défaut).

## Tests

### 1. Démarrer les services
```bash
docker compose down
docker compose up -d
```

### 2. Test des Sessions Collantes pour l'API

Exécutez ces commandes dans des terminaux séparés pour voir les logs en temps réel :

```bash
# Terminal 1 - Voir les logs de l'API
docker compose logs -f api

# Terminal 2 - Faire des requêtes à l'API
# Première requête pour obtenir le cookie
curl -v http://localhost/api

# Copier la valeur du cookie "api_sticky" de la réponse
# Faire plusieurs requêtes avec le même cookie
curl -v -b "api_sticky=VALEUR_DU_COOKIE" http://localhost/api
```

### 3. Test du Round-Robin pour le Serveur Web

```bash
# Terminal 1 - Voir les logs du serveur web
docker compose logs -f web

# Terminal 2 - Faire plusieurs requêtes au serveur web
for i in {1..10}; do curl http://localhost/; done
```

## Résultats Attendus

### Pour l'API (Sessions Collantes)
- La première requête crée un cookie "api_sticky"
- Les requêtes suivantes avec le même cookie sont dirigées vers la même instance
- Les logs montrent toutes les requêtes d'une session allant vers la même instance

### Pour le Serveur Web (Round-Robin)
- Les requêtes sont distribuées de manière équilibrée entre web-1 et web-2
- Pas de cookie de session dans les réponses

# Étape 7 : Sécurisation avec HTTPS

## Objectif
L'objectif de cette étape est de sécuriser notre infrastructure en utilisant HTTPS pour toutes les communications entre le navigateur et le reverse proxy Traefik.

## Configuration

### 1. Génération des Certificats

Pour générer les certificats SSL/TLS locaux :

```bash
# Création du répertoire pour les certificats
mkdir certificates

# Génération de la clé privée et du certificat
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout certificates/cert.key \
  -out certificates/cert.crt \
  -subj "/CN=localhost"
```

### 2. Configuration détaillée de Traefik

Le fichier `traefik.yaml` est le fichier de configuration principal de Traefik. Voici son contenu détaillé :

```yaml
# Define entrypoints for HTTP and HTTPS
entryPoints:
  web:
    address: ":80"    # Point d'entrée HTTP standard
    http:
      redirections:
        entryPoint:
          to: websecure    # Redirection vers HTTPS
          scheme: https    # Utilisation du schéma HTTPS
  websecure:
    address: ":443"   # Point d'entrée HTTPS standard

# Enable the Docker provider
providers:
  docker:
    exposedByDefault: false    # Les conteneurs ne sont pas exposés par défaut

# Define TLS settings using the self-signed certificate
tls:
  certificates:
    certFile: /etc/traefik/certificates/traefik.crt    # Chemin vers le certificat
    keyFile: /etc/traefik/certificates/traefik.key     # Chemin vers la clé privée

# Configure the Traefik dashboard
api:
  dashboard: true     # Active le dashboard Traefik
  insecure: true     # Permet l'accès non sécurisé au dashboard (uniquement pour le développement)
```

#### Explication des sections :

1. **Points d'entrée (entryPoints)**
   - `web` : Configure le point d'entrée HTTP sur le port 80
     - Inclut une redirection automatique vers HTTPS
   - `websecure` : Configure le point d'entrée HTTPS sur le port 443
     - Gère tout le trafic HTTPS sécurisé

2. **Provider Docker**
   - Active l'intégration avec Docker
   - `exposedByDefault: false` : Sécurité par défaut, nécessite une activation explicite des services

3. **Configuration TLS**
   - Définit les chemins vers les certificats SSL/TLS
   - Utilise des certificats auto-signés pour le développement
   - En production, ces certificats devraient être remplacés par des certificats valides

4. **API et Dashboard**
   - Active l'interface web de monitoring Traefik
   - Mode non sécurisé activé pour le développement
   - En production, il est recommandé de sécuriser ou désactiver le dashboard

### 3. Mise à jour de Docker Compose

Le fichier `docker-compose.yml` a été mis à jour pour :
- Monter les certificats SSL
- Configurer les points d'entrée HTTPS
- Activer TLS pour chaque service

```yaml
services:
  reverse_proxy:
    volumes:
      - ./certificates:/etc/certs
    # ... autres configurations ...

  web:
    labels:
      - "traefik.http.routers.web.tls=true"
      # ... autres labels ...

  api:
    labels:
      - "traefik.http.routers.api.tls=true"
      # ... autres labels ...
```

### 4. Configuration de la Redirection HTTP vers HTTPS

Dans `traefik.yaml`, ajout de la redirection automatique :

```yaml
http:
      redirections:
        entryPoint:
          to: websecure
          scheme: https
```

## Tests et Vérification

### 1. Test des Endpoints HTTPS

```bash
# Test du site web statique
curl -k https://localhost/

# Test de l'API
curl -k https://localhost/api
```

### 2. Vérification de la Redirection HTTP

```bash
# La requête HTTP devrait être redirigée vers HTTPS
curl -L http://localhost/
```

## Critères d'Acceptation

✅ Certificats SSL générés et configurés  
✅ HTTPS activé sur tous les services  
✅ Redirection HTTP vers HTTPS fonctionnelle  
✅ Communication sécurisée entre les services

## Sécurité

Points importants concernant la sécurité :

1. **Certificats SSL/TLS** :
   - Utilisation de certificats auto-signés pour le développement
   - En production, utiliser des certificats valides d'une autorité de certification

2. **Configuration HTTPS** :
   - Tous les services utilisent HTTPS
   - Redirection automatique de HTTP vers HTTPS
   - Protection contre les attaques man-in-the-middle

3. **Bonnes Pratiques** :
   - Renouvellement régulier des certificats
   - Configuration appropriée des en-têtes de sécurité
   - Utilisation de protocoles et chiffrements sécurisés

## Mise à jour des liens API

Pour assurer une intégration complète avec HTTPS, nous avons mis à jour tous les liens vers l'API dans le site web statique :
- Modification du protocole vers HTTPS
- Suppression du port explicite (8081)
- Utilisation du routage via Traefik (`https://localhost/api`)

Ces changements ont été appliqués dans tous les fichiers HTML du site statique pour garantir une expérience utilisateur cohérente et sécurisée.

```
