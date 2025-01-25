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

## Mise à jour du site statique

Les liens vers l'API dans le site web statique ont été mis à jour pour utiliser HTTPS :

### 1. Modifications des URLs

Dans tous les fichiers HTML (`index.html`, `contact.html`, `trainer.html`, `why.html`), les liens vers l'API ont été modifiés :

Avant :
```html
<a class="nav-link" href="http://localhost:8081/api" target="_blank">API</a>
```

Après :
```html
<a class="nav-link" href="https://localhost/api" target="_blank">API</a>
```

Changements effectués :
- Protocole changé de `http://` à `https://`
- Port 8081 supprimé car Traefik gère maintenant le routage
- Chemin `/api` maintenu pour la cohérence avec la configuration de Traefik

### 2. Impact des changements

Ces modifications assurent que :
- Toutes les communications sont sécurisées via HTTPS
- Les requêtes passent correctement par le reverse proxy Traefik
- Les sticky sessions sont maintenues pour l'API
- L'expérience utilisateur est cohérente avec le reste de l'infrastructure sécurisée

# Étape 7 : Sécurisation avec HTTPS

## Objectif
L'objectif de cette étape est de sécuriser notre infrastructure en utilisant HTTPS pour toutes les communications entre le navigateur et le reverse proxy Traefik.

## Configuration

### 1. Configuration de Traefik (`traefik.yaml`)

```yaml
entryPoints:
  web:
    address: ":80"
    http:
      redirections:
        entryPoint:
          to: websecure
          scheme: https
  websecure:
    address: ":443"

tls:
  certificates:
    - certFile: "/etc/certs/cert.crt"
      keyFile: "/etc/certs/cert.key"
```

Cette configuration :
- Définit deux points d'entrée : `web` (port 80) et `websecure` (port 443)
- Configure la redirection automatique de HTTP vers HTTPS
- Spécifie l'emplacement des certificats SSL

### 2. Configuration Docker Compose (`docker-compose.yml`)

```yaml
services:
  reverse-proxy:
    # ... autres configurations ...
    ports:
      - "80:80"
      - "443:443"
      - "8080:8080"
    volumes:
      - ./certificates:/etc/certs
      - ./traefik.yaml:/etc/traefik/traefik.yaml

  web:
    # ... autres configurations ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.web.rule=Host(`localhost`) && PathPrefix(`/`)"
      - "traefik.http.routers.web.entrypoints=websecure"
      - "traefik.http.routers.web.tls=true"

  todo-api:
    # ... autres configurations ...
    labels:
      - "traefik.enable=true"
      - "traefik.http.routers.api.rule=Host(`localhost`) && PathPrefix(`/api`)"
      - "traefik.http.routers.api.entrypoints=websecure"
      - "traefik.http.routers.api.tls=true"
```

Modifications principales :
- Ajout du port 443 pour HTTPS
- Montage des certificats SSL
- Configuration TLS pour les services web et API
- Définition des règles de routage avec Host et PathPrefix

## Points de vérification

Pour vérifier que la configuration HTTPS fonctionne correctement :

1. Accès aux services :
   - Site web : https://localhost
   - API : https://localhost/api
   - Dashboard Traefik : http://localhost:8080

2. Vérifications :
   - Le navigateur affiche le cadenas HTTPS
   - La redirection HTTP vers HTTPS fonctionne
   - Les certificats SSL sont correctement chargés

## Résolution des problèmes courants

1. Erreur de certificat :
   - Vérifier que les certificats sont correctement montés dans `/etc/certs`
   - Vérifier les permissions des fichiers de certificats

2. Erreur de redirection :
   - Vérifier la configuration des points d'entrée dans `traefik.yaml`
   - Vérifier les labels des services dans `docker-compose.yml`

3. Service inaccessible :
   - Vérifier que les ports 80 et 443 sont libres
   - Vérifier les logs de Traefik avec `docker logs traefik`

## Sécurité

Points importants concernant la sécurité :
- Les certificats utilisés sont auto-signés (acceptables en développement)
- La redirection HTTP vers HTTPS est obligatoire
- Le dashboard Traefik reste accessible en HTTP (port 8080)
- Les sticky sessions sont maintenues pour l'API via HTTPS
