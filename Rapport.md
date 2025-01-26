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

## Dépannage

1. **Dashboard inaccessible**
   - Vérifier que le port 8080 n'est pas utilisé
   - Vérifier les logs Traefik : `docker compose logs reverse_proxy`

2. **Services inaccessibles**
   - Vérifier les labels Traefik
   - Vérifier que les services sont en cours d'exécution
   - Consulter le dashboard pour l'état des routeurs

## Critères d'Acceptation

✅ Traefik configuré et fonctionnel  
✅ Dashboard accessible  
✅ Routage correct vers le site web statique  
✅ Routage correct vers l'API  
✅ Documentation complète  
✅ Tests de fonctionnement validés

## Mise à jour du site statique

Les liens vers l'API dans le site web statique ont été mis à jour pour utiliser HTTPS :

### 1. Modifications des URLs

Dans tous les fichiers HTML (`index.html`, `contact.html`, `trainer.html`, `why.html`), les liens vers l'API ont été modifiés :

Avant :
```html
<a class="nav-link" href="http://localhost:8081/api" target="_blank">API</a>
```

Après :
```html
<a class="nav-link" href="https://localhost/api" target="_blank">API</a>
```

Changements effectués :
- Protocole changé de `http://` à `https://`
- Port 8081 supprimé car Traefik gère maintenant le routage
- Chemin `/api` maintenu pour la cohérence avec la configuration de Traefik

### 2. Impact des changements

Ces modifications assurent que :
- Toutes les communications sont sécurisées via HTTPS
- Les requêtes passent correctement par le reverse proxy Traefik
- Les sticky sessions sont maintenues pour l'API
- L'expérience utilisateur est cohérente avec le reste de l'infrastructure sécurisée


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

## Dépannage

Si les sessions collantes ne fonctionnent pas :
1. Vérifiez que le cookie est bien présent dans la réponse de l'API
2. Assurez-vous que le cookie est correctement envoyé dans les requêtes suivantes
3. Vérifiez les logs Traefik pour des erreurs éventuelles :
```bash
docker compose logs reverse_proxy
```

# Étape 7 : Sécurisation avec HTTPS

## Objectif
L'objectif de cette étape est de sécuriser notre infrastructure en utilisant HTTPS pour toutes les communications entre le navigateur et le reverse proxy Traefik.

## Configuration

### 1. Configuration de Traefik (`traefik.yaml`)

```yaml
entryPoints:
  web:
    address: ":80"
    http:
      redirections:
        entryPoint:
          to: websecure
          scheme: https
  websecure:
    address: ":443"

tls:
  certificates:
    - certFile: "/etc/certs/cert.crt"
      keyFile: "/etc/certs/cert.key"
```

Cette configuration :
- Définit deux points d'entrée : `web` (port 80) et `websecure` (port 443)
- Configure la redirection automatique de HTTP vers HTTPS
- Spécifie l'emplacement des certificats SSL

### 2. Configuration Docker Compose (`docker-compose.yml`)

```yaml
services:
  reverse-proxy:
    # ... autres configurations ...
    ports:
      - "80:80"
      - "443:443"
      - "8080:8080"
    volumes:
      - ./certificates:/etc/certs
      - ./traefik.yaml:/etc/traefik/traefik.yaml

  web:
    # ... autres configurations ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.web.rule=Host(`localhost`) && PathPrefix(`/`)"
      - "traefik.http.routers.web.entrypoints=websecure"
      - "traefik.http.routers.web.tls=true"

  todo-api:
    # ... autres configurations ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.api.rule=Host(`localhost`) && PathPrefix(`/api`)"
      - "traefik.http.routers.api.entrypoints=websecure"
      - "traefik.http.routers.api.tls=true"
```

Modifications principales :
- Ajout du port 443 pour HTTPS
- Montage des certificats SSL
- Configuration TLS pour les services web et API
- Définition des règles de routage avec Host et PathPrefix

## Points de vérification

Pour vérifier que la configuration HTTPS fonctionne correctement :

1. Accès aux services :
   - Site web : https://localhost
   - API : https://localhost/api
   - Dashboard Traefik : http://localhost:8080

2. Vérifications :
   - Le navigateur affiche le cadenas HTTPS
   - La redirection HTTP vers HTTPS fonctionne
   - Les certificats SSL sont correctement chargés

## Résolution des problèmes courants

1. Erreur de certificat :
   - Vérifier que les certificats sont correctement montés dans `/etc/certs`
   - Vérifier les permissions des fichiers de certificats

2. Erreur de redirection :
   - Vérifier la configuration des points d'entrée dans `traefik.yaml`
   - Vérifier les labels des services dans `docker-compose.yml`

3. Service inaccessible :
   - Vérifier que les ports 80 et 443 sont libres
   - Vérifier les logs de Traefik avec `docker logs traefik`

## Sécurité

Points importants concernant la sécurité :
- Les certificats utilisés sont auto-signés (acceptables en développement)
- La redirection HTTP vers HTTPS est obligatoire
- Le dashboard Traefik reste accessible en HTTP (port 8080)
- Les sticky sessions sont maintenues pour l'API via HTTPS


# Étape 7 : Sécurisation avec HTTPS

## Objectif

L'objectif de cette étape était de sécuriser notre infrastructure en implémentant HTTPS pour toutes les communications entre le navigateur et notre reverse proxy Traefik.

## Modifications effectuées

1. Configuration de Traefik :
   - Ajout du point d'entrée `websecure` sur le port 443
   - Configuration de la redirection HTTP vers HTTPS
   - Configuration des certificats SSL

2. Mise à jour des services :
   - Activation de TLS pour le site web statique et l'API
   - Configuration des règles de routage avec Host et PathPrefix
   - Maintien des sticky sessions pour l'API en HTTPS

## Validation
- Le site web est accessible via https://localhost
- L'API est accessible via https://localhost/api
- La redirection HTTP vers HTTPS fonctionne correctement
- Les certificats SSL sont correctement chargés

## Procédure suivie

### 1. Génération des certificats SSL

Nous avons généré des certificats SSL auto-signés pour le développement en utilisant OpenSSL. Les certificats ont été stockés dans le dossier `certificates/`.

### 2. Configuration de Traefik

Nous avons configuré Traefik pour :
- Rediriger automatiquement tout le trafic HTTP vers HTTPS
- Utiliser le point d'entrée `websecure` sur le port 443
- Utiliser les certificats SSL générés

### 3. Vérification

Pour vérifier que la configuration HTTPS fonctionne correctement, nous avons :
1. Accédé au site via HTTPS (`https://localhost`)
2. Vérifié la présence du cadenas dans la barre d'adresse
3. Confirmé que la redirection HTTP vers HTTPS fonctionne

Pour plus de détails sur la configuration et le dépannage, voir la documentation complète dans le dossier `certificates/README.md`.

Pour plus de détails sur la configuration et le dépannage, voir la documentation complète dans `traefik/README.md`.

## Mise à jour des liens API

Pour assurer une intégration complète avec HTTPS, nous avons mis à jour tous les liens vers l'API dans le site web statique :
- Modification du protocole vers HTTPS
- Suppression du port explicite (8081)
- Utilisation du routage via Traefik (`https://localhost/api`)

Ces changements ont été appliqués dans tous les fichiers HTML du site statique pour garantir une expérience utilisateur cohérente et sécurisée.
