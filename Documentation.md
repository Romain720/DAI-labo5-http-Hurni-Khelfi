# Documentation de l'API Todo et Configuration Docker

## Table des matières
1. [Structure du Projet](#structure-du-projet)
2. [API REST Todo](#api-rest-todo)
3. [Configuration Maven](#configuration-maven)
4. [Configuration Docker](#configuration-docker)
5. [Déploiement](#déploiement)
6. [Reverse Proxy avec Traefik](#reverse-proxy-avec-traefik)

## 1. Structure du Projet

Le projet est composé de deux parties principales :
1. Une API REST Todo en Java avec Javalin
2. Un serveur web statique avec Nginx

### Structure des fichiers
```
project/
├── labo5-http-code-java/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── org/
│   │               └── example/
│   │                   ├── Main.java
│   │                   ├── Todo.java
│   │                   └── TodoService.java
│   ├── Dockerfile
│   └── pom.xml
├── static-web-server/
│   ├── static-web-server/
│   │   ├── index.html
│   │   ├── why.html
│   │   ├── trainer.html
│   │   └── contact.html
│   └── Dockerfile
└── docker-compose.yml
```

## 2. API REST Todo

### Endpoints
- `GET /api` : Récupérer tous les todos
- `GET /api/{id}` : Récupérer un todo spécifique
- `POST /api` : Créer un nouveau todo
- `PUT /api/{id}` : Mettre à jour un todo existant
- `DELETE /api/{id}` : Supprimer un todo

### Modèle Todo
```java
public class Todo {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
```

### Format JSON
```json
{
    "title": "Titre du todo",
    "description": "Description du todo",
    "completed": false
}
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

## Configuration Docker

### API Java (Dockerfile)
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

### Serveur Web Statique (Dockerfile)
```dockerfile
FROM nginx:latest
COPY static-web-server/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

### Docker Compose
```yaml
version: '3.8'

services:
  web:
    build:
      context: ./static-web-server
    ports:
      - "8080:80"
    container_name: static-web-server

  api:
    build:
      context: ./labo5-http-code-java
    ports:
      - "8081:8081"
    container_name: todo-api
```

## Déploiement

1. **Construction et démarrage des conteneurs**
   ```bash
   docker-compose up --build
   ```

2. **Arrêt des conteneurs**
   ```bash
   docker-compose down
   ```

3. **Accès aux services**
   - Site Web : http://localhost/
   - API : http://localhost/api
   
### Exemples d'utilisation de l'API

1. **Créer un nouveau todo**
   ```bash
   curl -X POST -H "Content-Type: application/json" \
        -d '{"title":"Nouvelle tâche","description":"Description","completed":false}' \
        http://localhost:8081/api
   ```

2. **Récupérer tous les todos**
   ```bash
   curl http://localhost:8081/api
   ```

3. **Mettre à jour un todo**
   ```bash
   curl -X PUT -H "Content-Type: application/json" \
        -d '{"title":"Tâche modifiée","description":"Nouvelle description","completed":true}' \
        http://localhost:8081/api/1
   ```

4. **Supprimer un todo**
   ```bash
   curl -X DELETE http://localhost:8081/api/1
   ```

## Reverse Proxy avec Traefik

### Configuration

Le reverse proxy Traefik est configuré pour :
1. Router le trafic vers les services appropriés
2. Fournir un tableau de bord pour la surveillance
3. S'intégrer avec Docker pour la découverte de services

#### Points d'accès
- Site web statique : `http://localhost/`
- API : `http://localhost/api`
- Dashboard Traefik : `http://localhost:8080`

#### Configuration Traefik
```yaml
reverse_proxy:
  image: traefik:v2.10
  command:
    - "--api.insecure=true"           # Active le dashboard (en mode non sécurisé pour le développement)
    - "--providers.docker=true"        # Active l'intégration Docker
    - "--providers.docker.exposedbydefault=false"  # Désactive l'exposition automatique des services
    - "--entrypoints.web.address=:80"  # Configure le point d'entrée web sur le port 80
  ports:
    - "80:80"      # Port pour le trafic web
    - "8080:8080"  # Port pour le dashboard
  volumes:
    - /var/run/docker.sock:/var/run/docker.sock:ro  # Accès au socket Docker (lecture seule)
```

#### Configuration des Services

##### Service Web Statique
```yaml
labels:
  - "traefik.enable=true"  # Active Traefik pour ce service
  - "traefik.http.routers.web.rule=PathPrefix(`/`)"  # Route tout le trafic racine vers ce service
  - "traefik.http.routers.web.entrypoints=web"  # Utilise le point d'entrée web
  - "traefik.http.services.web.loadbalancer.server.port=80"  # Port du service
```

##### Service API
```yaml
labels:
  - "traefik.enable=true"  # Active Traefik pour ce service
  - "traefik.http.routers.api.rule=PathPrefix(`/api`)"  # Route le trafic /api vers ce service
  - "traefik.http.routers.api.entrypoints=web"  # Utilise le point d'entrée web
  - "traefik.http.services.api.loadbalancer.server.port=8081"  # Port du service
```

### Sécurité
L'utilisation d'un reverse proxy améliore la sécurité de l'infrastructure de plusieurs manières :
1. **Isolation** : Les services backend ne sont pas directement exposés à Internet
2. **Point d'entrée unique** : Facilite la mise en place de règles de sécurité centralisées
3. **Masquage de l'infrastructure** : Les détails de l'infrastructure interne sont cachés aux clients

### Utilisation
1. Démarrer l'infrastructure :
   ```bash
   docker compose up -d
   ```
2. Accéder aux services :
   - Site web : http://localhost/
   - API : http://localhost/api
   - Dashboard Traefik : http://localhost:8080
