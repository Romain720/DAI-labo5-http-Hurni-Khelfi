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
