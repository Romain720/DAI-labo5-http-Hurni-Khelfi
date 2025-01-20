# Étape 1 : Utiliser une image de base Maven pour la compilation
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

# Étape 2 : Définir le répertoire de travail
WORKDIR /app

# Étape 3 : Copier le code source dans l'image
COPY . .

# Étape 4 : Exécuter la commande de compilation
RUN mvn package

# Étape 5 : Utiliser une image de base Java pour l'exécution
FROM openjdk:21

# Étape 6 : Définir le répertoire de travail
WORKDIR /app

# Étape 7 : Copier le fichier JAR compilé dans l'image
COPY --from=build /app/target/labo5-http-code-java-1.0-SNAPSHOT.jar app.jar

# Étape 8 : Exposer le port d'écoute
EXPOSE 8081

# Étape 9 : Commande pour exécuter l'application
CMD ["java", "-jar", "app.jar"]
