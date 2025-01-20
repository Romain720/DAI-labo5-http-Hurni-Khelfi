# Documentation de l'API Todo et Configuration Docker

## Table des matières
1. [Structure du Projet](#structure-du-projet)
2. [API REST Todo](#api-rest-todo)
3. [Configuration Maven](#configuration-maven)
4. [Configuration Docker](#configuration-docker)
5. [Déploiement](#déploiement)

## Structure du Projet

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

## API REST Todo

### Endpoints
- `GET /api/todos` : Récupérer tous les todos
- `GET /api/todos/{id}` : Récupérer un todo spécifique
- `POST /api/todos` : Créer un nouveau todo
- `PUT /api/todos/{id}` : Mettre à jour un todo existant
- `DELETE /api/todos/{id}` : Supprimer un todo

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
   - Site Web : http://localhost:8080
   - API : http://localhost:8081/api/todos

### Exemples d'utilisation de l'API

1. **Créer un nouveau todo**
   ```bash
   curl -X POST -H "Content-Type: application/json" \
        -d '{"title":"Nouvelle tâche","description":"Description","completed":false}' \
        http://localhost:8081/api/todos
   ```

2. **Récupérer tous les todos**
   ```bash
   curl http://localhost:8081/api/todos
   ```

3. **Mettre à jour un todo**
   ```bash
   curl -X PUT -H "Content-Type: application/json" \
        -d '{"title":"Tâche modifiée","description":"Nouvelle description","completed":true}' \
        http://localhost:8081/api/todos/1
   ```

4. **Supprimer un todo**
   ```bash
   curl -X DELETE http://localhost:8081/api/todos/1
   ```

## Notes importantes

1. **Ports**
   - Le serveur web utilise le port 80 en interne, mappé sur le port 8080 en externe
   - L'API utilise le port 8081 en interne et externe

2. **CORS**
   - L'API est configurée pour accepter les requêtes de n'importe quelle origine (CORS enabled)

3. **Persistance**
   - Les données sont stockées en mémoire et sont perdues lors du redémarrage du conteneur
   - Pour une utilisation en production, il faudrait ajouter une base de données persistante
