# Étape 1 : Site Web Statique avec Nginx

Ce document décrit la configuration et le déploiement du serveur web statique utilisant Nginx.

## Structure du Projet

```
static-web-server/
├── Dockerfile              # Configuration Docker pour Nginx
├── nginx.conf             # Configuration Nginx
└── static-web-server/     # Contenu statique
    ├── index.html         # Page principale
    ├── why.html          # Pages additionnelles
    ├── trainer.html
    └── contact.html
```

## Configuration Nginx

Le fichier `nginx.conf` configure le serveur web :

```nginx
events {
    worker_connections 1024;  # Nombre maximum de connexions simultanées par worker
}

http {
    include       mime.types;              # Inclut les types MIME standards
    default_type  application/octet-stream; # Type par défaut pour les fichiers inconnus
    
    server {
        listen 80;                # Port d'écoute
        location / {
            root /usr/share/nginx/html;  # Répertoire racine des fichiers statiques
            index index.html;            # Fichier par défaut
        }
    }
}
```

### Explication de la Configuration

1. **Section `events`** :
   - `worker_connections 1024` : Définit le nombre maximum de connexions simultanées que chaque worker process peut gérer.

2. **Section `http`** :
   - `include mime.types` : Charge les types MIME standards pour servir correctement les différents types de fichiers.
   - `default_type` : Définit le type MIME par défaut pour les fichiers non reconnus.

3. **Section `server`** :
   - `listen 80` : Configure le serveur pour écouter sur le port 80 (HTTP standard).
   - `location /` : Définit la configuration pour la racine du site.
   - `root` : Spécifie le répertoire où sont stockés les fichiers statiques.
   - `index` : Définit le fichier à servir par défaut.

## Configuration Docker

Le `Dockerfile` configure l'image Docker :

```dockerfile
FROM nginx:latest                                     # Image de base Nginx
COPY static-web-server/ /usr/share/nginx/html         # Copie les fichiers statiques
COPY nginx.conf /etc/nginx/nginx.conf                 # Copie la configuration
EXPOSE 80                                            # Expose le port 80
```

### Explication du Dockerfile

1. `FROM nginx:latest` : Utilise l'image officielle Nginx comme base.
2. Premier `COPY` : Copie le contenu du site web dans le répertoire servi par Nginx.
3. Second `COPY` : Remplace la configuration Nginx par défaut par notre configuration personnalisée.
4. `EXPOSE` : Indique que le conteneur écoutera sur le port 80.

## Construction et Déploiement

1. **Construire l'image** :
   ```bash
   docker build -t static-web-server .
   ```

2. **Lancer le conteneur** :
   ```bash
   docker run -d -p 8080:80 static-web-server
   ```

3. **Accès au site** :
   - Ouvrir un navigateur et accéder à `http://localhost:8080`

## Vérification des Critères d'Acceptation

✅ Dossier séparé créé pour le serveur web statique  
✅ Dockerfile basé sur l'image Nginx avec copie du contenu statique  
✅ Configuration nginx.conf pour servir le contenu sur le port 80  
✅ Documentation détaillée de la configuration  
✅ Image fonctionnelle accessible via navigateur  
✅ Documentation complète dans ce README
