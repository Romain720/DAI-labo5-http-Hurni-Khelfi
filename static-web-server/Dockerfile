# Utiliser l'image officielle Nginx comme base
FROM nginx:latest

# Copier le contenu statique dans le répertoire par défaut de Nginx
COPY static-web-server/ /usr/share/nginx/html

# Copier le fichier de configuration nginx.conf dans le conteneur
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port 80
EXPOSE 80