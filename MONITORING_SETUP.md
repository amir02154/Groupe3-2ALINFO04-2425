# Configuration Monitoring Prometheus + Grafana

## Vue d'ensemble

Cette configuration établit un système de monitoring complet avec :
- **Prometheus** : Collecte de métriques
- **Grafana** : Visualisation et alertes
- **AlertManager** : Gestion des notifications

## Architecture

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Grafana   │◄──►│ Prometheus  │◄──►│ Application │
│   (3000)    │    │   (9090)    │    │   (8086)    │
└─────────────┘    └─────────────┘    └─────────────┘
       │                   │                   │
       │                   ▼                   │
       │            ┌─────────────┐           │
       └───────────►│AlertManager │◄──────────┘
                    │   (9093)    │
                    └─────────────┘
```

## Configuration Prometheus

### `prometheus/prometheus.yml`
- Scrape l'application Spring Boot sur `/actuator/prometheus`
- Collecte les métriques JVM, HTTP, et base de données
- Intervalle de scrape : 10s pour l'application

### `prometheus/alert_rules.yml`
Règles d'alerte configurées :
- **ApplicationDown** : Application non disponible
- **HighCPUUsage** : CPU > 80%
- **HighMemoryUsage** : Mémoire > 85%
- **HighErrorRate** : Taux d'erreur > 10%
- **HighResponseTime** : Temps de réponse > 2s
- **DatabaseConnectionIssues** : Pool de connexions > 80%

## Configuration Grafana

### Datasource
- **Prometheus** configuré automatiquement
- URL : `http://prometheus:9090`
- Intervalle de requête : 15s

### Dashboards

#### 1. Application Monitoring Dashboard
**Panels inclus :**
- Application Status (statut up/down)
- HTTP Request Rate (requêtes/sec)
- Response Time 95th percentile
- Error Rate (4xx, 5xx)
- JVM Memory Usage
- Database Connections
- System CPU Usage
- Active Alerts Table

#### 2. Alerts & Monitoring Dashboard
**Panels inclus :**
- Alert Status Overview
- Active Alerts by Severity (pie chart)
- Application Health Score (gauge)
- Error Rate Trend
- System Resources Alert Status
- Response Time Alert Status
- Database Health
- Prometheus Targets Status

## Configuration AlertManager

### `alertmanager/alertmanager.yml`
- **Routing** : Alerte par sévérité (critical/warning)
- **Receivers** : Email et webhook
- **Inhibition** : Les alertes critiques suppriment les warnings

## Déploiement

### 1. Démarrer les services
```bash
docker compose up -d
```

### 2. Configurer le monitoring
```bash
chmod +x scripts/setup-monitoring.sh
./scripts/setup-monitoring.sh
```

### 3. Tester la configuration
```bash
chmod +x scripts/test-monitoring.sh
./scripts/test-monitoring.sh
```

## URLs d'accès

- **Grafana** : http://172.29.215.125:3000 (admin/123456aA)
- **Prometheus** : http://172.29.215.125:9090
- **AlertManager** : http://172.29.215.125:9093
- **Application** : http://172.29.215.125:8086

## Métriques collectées

### Métriques HTTP
- `http_requests_total` : Nombre total de requêtes
- `http_request_duration_seconds` : Durée des requêtes

### Métriques JVM
- `jvm_memory_used_bytes` : Mémoire utilisée
- `jvm_memory_max_bytes` : Mémoire maximale
- `process_cpu_seconds_total` : CPU utilisé

### Métriques Base de données
- `hikaricp_connections_active` : Connexions actives
- `hikaricp_connections_idle` : Connexions inactives
- `hikaricp_connections_max` : Connexions maximales

### Métriques système
- `up` : Statut des targets Prometheus

## Alertes configurées

### Alertes critiques
- **ApplicationDown** : Application non disponible > 1 minute

### Alertes warnings
- **HighCPUUsage** : CPU > 80% > 2 minutes
- **HighMemoryUsage** : Mémoire > 85% > 2 minutes
- **HighErrorRate** : Taux d'erreur > 10% > 2 minutes
- **HighResponseTime** : Temps de réponse > 2s > 2 minutes
- **DatabaseConnectionIssues** : Pool de connexions > 80% > 2 minutes

## Scripts utilitaires

### `scripts/setup-monitoring.sh`
- Configure automatiquement Grafana
- Importe les dashboards
- Configure les datasources
- Crée les canaux de notification

### `scripts/test-monitoring.sh`
- Teste la connectivité des services
- Vérifie les métriques disponibles
- Valide les dashboards
- Contrôle les alertes

## Personnalisation

### Ajouter de nouvelles métriques
1. Ajouter les métriques dans l'application Spring Boot
2. Mettre à jour la configuration Prometheus si nécessaire
3. Créer de nouveaux panels dans Grafana
4. Ajouter des règles d'alerte si nécessaire

### Ajouter de nouveaux dashboards
1. Créer le fichier JSON du dashboard
2. Placer dans `grafana/dashboards/`
3. Le dashboard sera auto-provisionné

### Modifier les alertes
1. Éditer `prometheus/alert_rules.yml`
2. Redémarrer Prometheus
3. Les alertes seront automatiquement mises à jour

## Dépannage

### Prometheus ne collecte pas de métriques
- Vérifier que l'application expose `/actuator/prometheus`
- Contrôler la connectivité réseau
- Vérifier la configuration des targets

### Grafana ne peut pas se connecter à Prometheus
- Vérifier que Prometheus est accessible
- Contrôler la configuration de la datasource
- Vérifier les logs Grafana

### Les alertes ne se déclenchent pas
- Vérifier la syntaxe des règles d'alerte
- Contrôler que les métriques sont collectées
- Vérifier la configuration AlertManager

## Logs utiles

```bash
# Logs Prometheus
docker logs prometheus

# Logs Grafana
docker logs grafana

# Logs AlertManager
docker logs alertmanager

# Logs Application
docker logs app
``` 