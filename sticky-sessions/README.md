# Étape 6 : Sessions Collantes avec Traefik

Ce document décrit la configuration et la validation des sessions collantes (sticky sessions) pour l'API, tout en maintenant le round-robin pour le serveur web statique.

## Configuration

Dans le fichier `docker-compose.yml`, ajoutez les labels suivants pour le service API :

```yaml
services:
  api:
    # ... autres configurations ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.api.rule=PathPrefix(`/api`)"
      - "traefik.http.services.api.loadbalancer.server.port=8081"
      - "traefik.http.services.api.loadbalancer.sticky=true"
      - "traefik.http.services.api.loadbalancer.sticky.cookie=true"
      - "traefik.http.services.api.loadbalancer.sticky.cookie.name=api_sticky"
      - "traefik.http.services.api.loadbalancer.sticky.cookie.secure=false"
```

Le service web statique garde sa configuration d'origine (round-robin par défaut).

## Tests

### 1. Démarrer les services
```bash
docker compose down
docker compose up -d
```

### 2. Test des Sessions Collantes pour l'API

Exécutez ces commandes dans des terminaux séparés pour voir les logs en temps réel :

```bash
# Terminal 1 - Voir les logs de l'API
docker compose logs -f api

# Terminal 2 - Faire des requêtes à l'API
# Première requête pour obtenir le cookie
curl -v http://localhost/api

# Copier la valeur du cookie "api_sticky" de la réponse
# Faire plusieurs requêtes avec le même cookie
curl -v -b "api_sticky=VALEUR_DU_COOKIE" http://localhost/api
```

### 3. Test du Round-Robin pour le Serveur Web

```bash
# Terminal 1 - Voir les logs du serveur web
docker compose logs -f web

# Terminal 2 - Faire plusieurs requêtes au serveur web
for i in {1..10}; do curl http://localhost/; done
```

## Résultats Attendus

### Pour l'API (Sessions Collantes)
- La première requête crée un cookie "api_sticky"
- Les requêtes suivantes avec le même cookie sont dirigées vers la même instance
- Les logs montrent toutes les requêtes d'une session allant vers la même instance

### Pour le Serveur Web (Round-Robin)
- Les requêtes sont distribuées de manière équilibrée entre web-1 et web-2
- Pas de cookie de session dans les réponses

## Dépannage

Si les sessions collantes ne fonctionnent pas :
1. Vérifiez que le cookie est bien présent dans la réponse de l'API
2. Assurez-vous que le cookie est correctement envoyé dans les requêtes suivantes
3. Vérifiez les logs Traefik pour des erreurs éventuelles :
```bash
docker compose logs reverse_proxy
