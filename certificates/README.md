# Étape 7 : Sécurisation avec HTTPS

## Objectif
L'objectif de cette étape est de sécuriser notre infrastructure en utilisant HTTPS pour chiffrer la communication entre le navigateur et le reverse proxy Traefik.

## Configuration

### 1. Génération des certificats SSL
Pour mettre en place HTTPS, nous avons besoin de certificats SSL. Dans un environnement de développement, nous utilisons des certificats auto-signés. Voici la procédure pour les générer :

```bash
# Création du répertoire pour les certificats
mkdir -p certificates

# Génération de la clé privée et du certificat auto-signé
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout certificates/cert.key \
  -out certificates/cert.crt \
  -subj "/CN=localhost"
```

### 2. Configuration de Traefik
Traefik est configuré pour utiliser HTTPS via le point d'entrée `websecure` sur le port 443. La configuration dans `traefik.yaml` inclut :

- Redirection automatique de HTTP vers HTTPS
- Point d'entrée sécurisé sur le port 443
- Utilisation des certificats SSL

### 3. Configuration Docker Compose
Le service Traefik dans `docker-compose.yml` est configuré pour monter les certificats SSL :

```yaml
services:
  reverse-proxy:
    volumes:
      - ./certificates:/etc/certs
```

## Validation

Pour vérifier que la configuration HTTPS fonctionne correctement :

1. Accéder à l'application via HTTPS : `https://localhost`
2. Vérifier la présence du cadenas dans la barre d'adresse du navigateur
3. Accepter l'avertissement de sécurité (normal pour un certificat auto-signé)
4. Vérifier que la redirection HTTP vers HTTPS fonctionne en accédant à `http://localhost`

## Considérations de sécurité

- En production, il est recommandé d'utiliser des certificats signés par une autorité de certification reconnue
- Les certificats auto-signés sont acceptables uniquement pour le développement
- La clé privée doit être protégée et ne jamais être partagée ou versionnée dans Git
- La redirection HTTP vers HTTPS est importante pour assurer que toutes les connexions sont sécurisées

## Troubleshooting

En cas de problèmes :
1. Vérifier que les certificats sont correctement montés dans le conteneur Traefik
2. Vérifier les logs de Traefik pour des erreurs de configuration
3. S'assurer que les ports 80 et 443 sont disponibles sur la machine hôte
4. Vérifier que les chemins vers les certificats dans la configuration sont corrects
