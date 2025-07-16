#!/bin/bash

echo "🔧 Setting up Monitoring Infrastructure..."

# Configuration
GRAFANA_URL="http://172.29.215.125:3000"
GRAFANA_USER="admin"
GRAFANA_PASS="123456aA"
PROMETHEUS_URL="http://172.29.215.125:9090"

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 30

# Wait for Prometheus
echo "Waiting for Prometheus..."
until curl -s "$PROMETHEUS_URL/-/healthy" > /dev/null; do
    echo "Waiting for Prometheus..."
    sleep 5
done
echo "✅ Prometheus is ready"

# Wait for Grafana
echo "Waiting for Grafana..."
until curl -s "$GRAFANA_URL/api/health" > /dev/null; do
    echo "Waiting for Grafana..."
    sleep 5
done
echo "✅ Grafana is ready"

# Wait for AlertManager
echo "Waiting for AlertManager..."
until curl -s "http://172.29.215.125:9093/-/healthy" > /dev/null; do
    echo "Waiting for AlertManager..."
    sleep 5
done
echo "✅ AlertManager is ready"

# Configure Prometheus datasource in Grafana
echo "Configuring Prometheus datasource..."
curl -s -X POST "$GRAFANA_URL/api/datasources" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS" \
    -d '{
        "name":"Prometheus",
        "type":"prometheus",
        "url":"http://prometheus:9090",
        "access":"proxy",
        "isDefault":true,
        "jsonData": {
            "timeInterval": "15s",
            "queryTimeout": "60s"
        }
    }'

# Import application monitoring dashboard
echo "Importing application monitoring dashboard..."
curl -s -X POST "$GRAFANA_URL/api/dashboards/import" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS" \
    -d @grafana/dashboards/application-monitoring.json

# Import alerts dashboard
echo "Importing alerts dashboard..."
curl -s -X POST "$GRAFANA_URL/api/dashboards/import" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS" \
    -d @grafana/dashboards/alerts-dashboard.json

# Create notification channel for alerts
echo "Creating notification channel..."
curl -s -X POST "$GRAFANA_URL/api/alert-notifications" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS" \
    -d '{
        "name": "Foyer Alerts Channel",
        "type": "email",
        "isDefault": true,
        "settings": {
            "addresses": "admin@foyer.com"
        }
    }'

# Reload provisioning
echo "Reloading Grafana provisioning..."
curl -s -X POST "$GRAFANA_URL/api/admin/provisioning/dashboards/reload" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS"

curl -s -X POST "$GRAFANA_URL/api/admin/provisioning/alerting/reload" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS"

echo "✅ Monitoring setup completed!"
echo ""
echo "📊 Monitoring URLs:"
echo "Grafana: http://172.29.215.125:3000 (admin/123456aA)"
echo "Prometheus: http://172.29.215.125:9090"
echo "AlertManager: http://172.29.215.125:9093"
echo "Application: http://172.29.215.125:8086" 