#!/bin/bash

echo "🧪 Testing Monitoring Setup..."

# Test Prometheus
echo "Testing Prometheus..."
if curl -s http://localhost:9090/-/healthy > /dev/null; then
    echo "✅ Prometheus is healthy"
    
    # Check targets
    echo "Prometheus targets:"
    curl -s http://localhost:9090/api/v1/targets | jq '.data.activeTargets[] | {job: .labels.job, health: .health}' 2>/dev/null || echo "No targets found"
else
    echo "❌ Prometheus is not responding"
fi

# Test Grafana
echo "Testing Grafana..."
if curl -s http://localhost:3000/api/health > /dev/null; then
    echo "✅ Grafana is healthy"
    
    # Check datasources
    echo "Grafana datasources:"
    curl -s http://localhost:3000/api/datasources -u admin:123456aA | jq '.[] | {name: .name, type: .type}' 2>/dev/null || echo "No datasources found"
    
    # Check dashboards
    echo "Grafana dashboards:"
    curl -s http://localhost:3000/api/search -u admin:123456aA | jq '.[] | {title: .title, type: .type}' 2>/dev/null || echo "No dashboards found"
else
    echo "❌ Grafana is not responding"
fi

# Test AlertManager
echo "Testing AlertManager..."
if curl -s http://localhost:9093/-/healthy > /dev/null; then
    echo "✅ AlertManager is healthy"
    
    # Check alert status
    echo "AlertManager alerts:"
    curl -s http://localhost:9093/api/v1/alerts | jq '.data[] | {alertname: .labels.alertname, state: .state}' 2>/dev/null || echo "No alerts found"
else
    echo "❌ AlertManager is not responding"
fi

# Test Application
echo "Testing Application..."
if curl -s http://localhost:8086/actuator/health > /dev/null; then
    echo "✅ Application is healthy"
    
    # Check metrics endpoint
    if curl -s http://localhost:8086/actuator/prometheus > /dev/null; then
        echo "✅ Application metrics endpoint is accessible"
    else
        echo "❌ Application metrics endpoint is not accessible"
    fi
else
    echo "❌ Application is not responding"
fi

echo "🎯 Monitoring test completed!" 