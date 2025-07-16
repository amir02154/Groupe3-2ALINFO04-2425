# Guide Rapide - Configuration Monitoring

## 🚀 Configuration avec Conteneurs Existants

Vous avez déjà des conteneurs Grafana et Prometheus qui tournent. Voici comment configurer le monitoring :

### 1. Rendre les scripts exécutables
```bash
chmod +x scripts/*.sh
```

### 2. Configurer le monitoring complet
```bash
./scripts/setup-existing-monitoring.sh
```

### 3. Tester la configuration
```bash
./scripts/test-monitoring.sh
```

## 📊 URLs d'accès

- **Grafana** : http://172.29.215.125:3000 (admin/123456aA)
- **Prometheus** : http://172.29.215.125:9090
- **Application** : http://172.29.215.125:8086

## 📋 Dashboards disponibles

### 1. Application Monitoring Dashboard
- Statut de l'application
- Taux de requêtes HTTP
- Temps de réponse (95th percentile)
- Taux d'erreurs
- Utilisation mémoire JVM
- Connexions base de données
- Utilisation CPU système
- Alertes actives

### 2. Alerts & Monitoring Dashboard
- Vue d'ensemble des alertes
- Alertes par sévérité (graphique circulaire)
- Score de santé de l'application
- Tendance du taux d'erreurs
- Statut des alertes de ressources
- Temps de réponse des alertes
- Santé de la base de données
- Statut des targets Prometheus

## 🔔 Alertes configurées

### Alertes critiques
- **ApplicationDown** : Application non disponible > 1 minute

### Alertes warnings
- **HighCPUUsage** : CPU > 80% > 2 minutes
- **HighMemoryUsage** : Mémoire > 85% > 2 minutes
- **HighErrorRate** : Taux d'erreur > 10% > 2 minutes
- **HighResponseTime** : Temps de réponse > 2s > 2 minutes
- **DatabaseConnectionIssues** : Pool de connexions > 80% > 2 minutes

## 🛠️ Scripts disponibles

- `scripts/setup-existing-monitoring.sh` : Configuration complète
- `scripts/configure-prometheus.sh` : Configuration Prometheus uniquement
- `scripts/configure-grafana.sh` : Configuration Grafana uniquement
- `scripts/test-monitoring.sh` : Test de la configuration
- `scripts/setup-monitoring.sh` : Configuration avec nouveaux conteneurs

## 🔧 Configuration manuelle

### Prometheus
```bash
# Copier la configuration
docker cp prometheus/prometheus.yml prometheus-p:/etc/prometheus/
docker cp prometheus/alert_rules.yml prometheus-p:/etc/prometheus/

# Recharger la configuration
curl -X POST http://172.29.215.125:9090/-/reload
```

### Grafana
```bash
# Configurer la datasource Prometheus
curl -X POST http://172.29.215.125:3000/api/datasources \
  -H "Content-Type: application/json" \
  -u admin:123456aA \
  -d '{
    "name":"Prometheus",
    "type":"prometheus",
    "url":"http://172.29.215.125:9090",
    "access":"proxy",
    "isDefault":true
  }'

# Importer les dashboards
curl -X POST http://172.29.215.125:3000/api/dashboards/import \
  -H "Content-Type: application/json" \
  -u admin:123456aA \
  -d @grafana/dashboards/application-monitoring.json

curl -X POST http://172.29.215.125:3000/api/dashboards/import \
  -H "Content-Type: application/json" \
  -u admin:123456aA \
  -d @grafana/dashboards/alerts-dashboard.json
```

## 🐛 Dépannage

### Prometheus ne collecte pas de métriques
```bash
# Vérifier les targets
curl http://172.29.215.125:9090/api/v1/targets

# Vérifier les règles d'alerte
curl http://172.29.215.125:9090/api/v1/rules
```

### Grafana ne peut pas se connecter à Prometheus
```bash
# Vérifier les datasources
curl http://172.29.215.125:3000/api/datasources -u admin:123456aA

# Vérifier les dashboards
curl http://172.29.215.125:3000/api/search -u admin:123456aA
```

### Application ne répond pas
```bash
# Vérifier la santé de l'application
curl http://172.29.215.125:8086/actuator/health

# Vérifier les métriques
curl http://172.29.215.125:8086/actuator/prometheus
```

## 📝 Notes importantes

- Les conteneurs existants sont : `grafana-p` et `prometheus-p`
- L'adresse IP utilisée est : `172.29.215.125`
- Les dashboards sont automatiquement importés
- Les alertes sont configurées dans Prometheus
- Les métriques sont collectées toutes les 10 secondes 