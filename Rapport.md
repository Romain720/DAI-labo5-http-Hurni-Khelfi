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
