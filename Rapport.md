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
