#!/bin/bash

# Configuration
GRAFANA_URL="http://localhost:3000"
GRAFANA_USER="admin"
GRAFANA_PASS="123456aA"
PROMETHEUS_DS_UID="prometheus"

# Wait for Grafana to be ready
echo "Waiting for Grafana to be ready..."
until curl -s "$GRAFANA_URL/api/health" > /dev/null; do
    sleep 5
done

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

# Import alert rules
echo "Importing alert rules..."
curl -s -X POST "$GRAFANA_URL/api/admin/provisioning/alerting/reload" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS"

# Import dashboards
echo "Importing dashboards..."
curl -s -X POST "$GRAFANA_URL/api/admin/provisioning/dashboards/reload" \
    -H "Content-Type: application/json" \
    -u "$GRAFANA_USER:$GRAFANA_PASS"

echo "Grafana alerts and dashboards configured successfully!" 