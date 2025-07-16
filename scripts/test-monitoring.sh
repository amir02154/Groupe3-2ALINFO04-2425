#!/bin/bash

echo "🧪 Testing Monitoring Setup..."

# Configuration
GRAFANA_URL="http://172.29.215.125:3000"
GRAFANA_USER="admin"
GRAFANA_PASS="123456aA"
PROMETHEUS_URL="http://172.29.215.125:9090"
ALERTMANAGER_URL="http://172.29.215.125:9093"

# Test Prometheus
echo "Testing Prometheus..."
if curl -s "$PROMETHEUS_URL/-/healthy" > /dev/null; then
    echo "✅ Prometheus is healthy"
    
    # Check targets
    echo "Prometheus targets:"
    curl -s "$PROMETHEUS_URL/api/v1/targets" | jq '.data.activeTargets[] | {job: .labels.job, health: .health}' 2>/dev/null || echo "No targets found"
    
    # Check metrics
    echo "Available metrics:"
    curl -s "$PROMETHEUS_URL/api/v1/label/__name__/values" | jq '.data[]' | head -10 2>/dev/null || echo "No metrics found"
else
    echo "❌ Prometheus is not responding"
fi

# Test Grafana
echo "Testing Grafana..."
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

# Test AlertManager
echo "Testing AlertManager..."
if curl -s "$ALERTMANAGER_URL/-/healthy" > /dev/null; then
    echo "✅ AlertManager is healthy"
    
    # Check alert status
    echo "AlertManager alerts:"
    curl -s "$ALERTMANAGER_URL/api/v1/alerts" | jq '.data[] | {alertname: .labels.alertname, state: .state}' 2>/dev/null || echo "No alerts found"
else
    echo "❌ AlertManager is not responding"
fi

# Test Application
echo "Testing Application..."
if curl -s "http://172.29.215.125:8086/actuator/health" > /dev/null; then
    echo "✅ Application is healthy"
    
    # Check metrics endpoint
    if curl -s "http://172.29.215.125:8086/actuator/prometheus" > /dev/null; then
        echo "✅ Application metrics endpoint is accessible"
        
        # Check for specific metrics
        echo "Available application metrics:"
        curl -s "http://172.29.215.125:8086/actuator/prometheus" | grep -E "(http_requests_total|jvm_memory_used_bytes|hikaricp_connections)" | head -5
    else
        echo "❌ Application metrics endpoint is not accessible"
    fi
else
    echo "❌ Application is not responding"
fi

# Test Prometheus targets
echo "Testing Prometheus targets..."
TARGETS=$(curl -s "$PROMETHEUS_URL/api/v1/targets" | jq '.data.activeTargets[] | {job: .labels.job, health: .health}' 2>/dev/null)
if [ -n "$TARGETS" ]; then
    echo "✅ Prometheus targets found:"
    echo "$TARGETS"
else
    echo "❌ No Prometheus targets found"
fi

echo "🎯 Monitoring test completed!" 