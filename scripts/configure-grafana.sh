#!/bin/bash

echo "🔧 Configuring Grafana with existing containers..."

# Configuration
GRAFANA_CONTAINER="grafana-p"
GRAFANA_URL="http://172.29.215.125:3000"
GRAFANA_USER="admin"
GRAFANA_PASS="123456aA"

# Wait for Grafana to be ready
echo "Waiting for Grafana to be ready..."
until curl -s "$GRAFANA_URL/api/health" > /dev/null; do
    echo "Waiting for Grafana..."
    sleep 5
done
echo "✅ Grafana is ready"

# Configure Prometheus datasource
echo "Configuring Prometheus datasource..."
curl -s -X POST "$GRAFANA_URL/api/datasources" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS" \
    -d '{
        "name":"Prometheus",
        "type":"prometheus",
        "url":"http://172.29.215.125:9090",
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

# Test configuration
echo "Testing Grafana configuration..."
if curl -s "$GRAFANA_URL/api/health" > /dev/null; then
    echo "✅ Grafana is healthy"
    
    # Check datasources
    echo "Grafana datasources:"
    curl -s "$GRAFANA_URL/api/datasources" -u "$GRAFANA_USER:$GRAFANA_PASS" | jq '.[] | {name: .name, type: .type}' 2>/dev/null || echo "No datasources found"
    
    # Check dashboards
    echo "Grafana dashboards:"
    curl -s "$GRAFANA_URL/api/search" -u "$GRAFANA_USER:$GRAFANA_PASS" | jq '.[] | {title: .title, type: .type}' 2>/dev/null || echo "No dashboards found"
else
    echo "❌ Grafana is not responding"
fi

echo "✅ Grafana configuration completed!"
echo ""
echo "📊 Grafana URL: http://172.29.215.125:3000 (admin/123456aA)" 