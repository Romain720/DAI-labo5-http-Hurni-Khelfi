# Étape 1 : Utiliser une image de base Java
FROM openjdk:21-jdk-slim

# Étape 2 : Définir le répertoire de travail
WORKDIR /app

# Étape 3 : Copier le fichier JAR dans l'image
COPY target/labo5-http-code-java-1.0-SNAPSHOT.jar app.jar

# Étape 4 : Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
