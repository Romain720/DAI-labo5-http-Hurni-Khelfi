# Étape 4 : Reverse Proxy avec Traefik

Ce document décrit la mise en place d'un reverse proxy Traefik pour gérer le routage entre le serveur web statique et l'API REST.

## Objectifs

1. Mettre en place un reverse proxy avec Traefik
2. Configurer le routage vers les différents services
3. Activer le dashboard Traefik pour la surveillance
4. Sécuriser l'infrastructure

## Architecture

```
                   ┌─────────────────┐
                   │                 │
                   │    Traefik      │
                   │  Reverse Proxy  │
                   │    (Port 80)    │
                   │                 │
                   └────────┬────────┘
                           │
                ┌─────────┴──────────┐
                │                    │
        ┌───────┴─────┐      ┌──────┴──────┐
        │             │      │             │
  ┌─────┴─────┐ ┌────┴────┐ │  ┌────────┐ │
  │  Static   │ │   API   │ │  │Traefik │ │
  │   Web     │ │ Service │ │  │Dashboard│ │
  │  Server   │ │(Port 81)│ │  │(8080)  │ │
  └───────────┘ └─────────┘ │  └────────┘ │
                            └──────────────┘
```

## Configuration Traefik

### docker-compose.yml

```yaml
version: '3.8'

services:
  reverse_proxy:
    image: traefik:v2.10
    command:
      - "--api.insecure=true"           # Active le dashboard
      - "--providers.docker=true"        # Active l'intégration Docker
      - "--providers.docker.exposedbydefault=false"
      - "--entrypoints.web.address=:80"  # Point d'entrée web
    ports:
      - "80:80"      # Port pour le trafic web
      - "8080:8080"  # Port pour le dashboard
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock:ro

  web:
    # ... configuration du service web ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.web.rule=PathPrefix(`/`)"
      - "traefik.http.services.web.loadbalancer.server.port=80"

  api:
    # ... configuration du service api ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.api.rule=PathPrefix(`/api`)"
      - "traefik.http.services.api.loadbalancer.server.port=8081"
```

## Explication de la Configuration

### 1. Service Traefik

#### Commandes
- `--api.insecure=true` : Active le dashboard Traefik (mode non sécurisé pour le développement)
- `--providers.docker=true` : Permet à Traefik de découvrir les services via Docker
- `--providers.docker.exposedbydefault=false` : Désactive l'exposition automatique des services
- `--entrypoints.web.address=:80` : Définit le point d'entrée web sur le port 80

#### Ports
- `80:80` : Port pour le trafic web
- `8080:8080` : Port pour accéder au dashboard Traefik

#### Volumes
- `/var/run/docker.sock:/var/run/docker.sock:ro` : Accès en lecture seule au socket Docker

### 2. Configuration des Services

#### Service Web Statique
```yaml
labels:
  - "traefik.enable=true"  # Active Traefik pour ce service
  - "traefik.http.routers.web.rule=PathPrefix(`/`)"  # Route le trafic racine
  - "traefik.http.services.web.loadbalancer.server.port=80"  # Port du service
```

#### Service API
```yaml
labels:
  - "traefik.enable=true"  # Active Traefik pour ce service
  - "traefik.http.routers.api.rule=PathPrefix(`/api`)"  # Route le trafic /api
  - "traefik.http.services.api.loadbalancer.server.port=8081"  # Port du service
```

## Points d'Accès

- Site Web Statique : `http://localhost/`
- API REST : `http://localhost/api`
- Dashboard Traefik : `http://localhost:8080`

## Avantages du Reverse Proxy

1. **Sécurité**
   - Isolation des services backend
   - Point d'entrée unique
   - Masquage de l'infrastructure interne

2. **Gestion**
   - Configuration centralisée
   - Surveillance via dashboard
   - Facilité de maintenance

3. **Flexibilité**
   - Routage dynamique
   - Découverte automatique des services
   - Scalabilité simplifiée

## Tests de Fonctionnement

1. **Vérifier le Dashboard**
   ```bash
   curl http://localhost:8080
   ```

2. **Tester le Site Web**
   ```bash
   curl http://localhost/
   ```

3. **Tester l'API**
   ```bash
   curl http://localhost/api
   ```

## Dépannage

1. **Dashboard inaccessible**
   - Vérifier que le port 8080 n'est pas utilisé
   - Vérifier les logs Traefik : `docker compose logs reverse_proxy`

2. **Services inaccessibles**
   - Vérifier les labels Traefik
   - Vérifier que les services sont en cours d'exécution
   - Consulter le dashboard pour l'état des routeurs

## Critères d'Acceptation

✅ Traefik configuré et fonctionnel  
✅ Dashboard accessible  
✅ Routage correct vers le site web statique  
✅ Routage correct vers l'API  
✅ Documentation complète  
✅ Tests de fonctionnement validés
