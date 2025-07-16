#!/bin/bash

echo "🔧 Configuring Prometheus with existing containers..."

# Configuration
PROMETHEUS_CONTAINER="prometheus-p"
PROMETHEUS_URL="http://172.29.215.125:9090"

# Copy configuration files to the running container
echo "Copying Prometheus configuration..."
docker cp prometheus/prometheus.yml $PROMETHEUS_CONTAINER:/etc/prometheus/
docker cp prometheus/alert_rules.yml $PROMETHEUS_CONTAINER:/etc/prometheus/

# Reload Prometheus configuration
echo "Reloading Prometheus configuration..."
curl -s -X POST "$PROMETHEUS_URL/-/reload"

# Wait for configuration to take effect
echo "Waiting for configuration to take effect..."
sleep 10

# Test configuration
echo "Testing Prometheus configuration..."
if curl -s "$PROMETHEUS_URL/-/healthy" > /dev/null; then
    echo "✅ Prometheus is healthy"
    
    # Check targets
    echo "Prometheus targets:"
    curl -s "$PROMETHEUS_URL/api/v1/targets" | jq '.data.activeTargets[] | {job: .labels.job, health: .health}' 2>/dev/null || echo "No targets found"
    
    # Check alert rules
    echo "Alert rules:"
    curl -s "$PROMETHEUS_URL/api/v1/rules" | jq '.data.groups[] | {name: .name, rules: [.rules[] | {name: .name, state: .state}]}' 2>/dev/null || echo "No rules found"
else
    echo "❌ Prometheus is not responding"
fi

echo "✅ Prometheus configuration completed!" 