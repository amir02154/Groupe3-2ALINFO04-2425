#!/bin/bash

echo "🚀 Setting up Monitoring with Existing Containers..."

# Configuration
GRAFANA_URL="http://172.29.215.125:3000"
PROMETHEUS_URL="http://172.29.215.125:9090"
ALERTMANAGER_URL="http://172.29.215.125:9093"

echo "📊 Using existing containers:"
echo "- Grafana: grafana-p (port 3000)"
echo "- Prometheus: prometheus-p (port 9090)"

# Step 1: Configure Prometheus
echo ""
echo "🔧 Step 1: Configuring Prometheus..."
./scripts/configure-prometheus.sh

# Step 2: Configure Grafana
echo ""
echo "🔧 Step 2: Configuring Grafana..."
./scripts/configure-grafana.sh

# Step 3: Test the complete setup
echo ""
echo "🔧 Step 3: Testing complete setup..."
./scripts/test-monitoring.sh

echo ""
echo "✅ Monitoring setup completed!"
echo ""
echo "📊 Access URLs:"
echo "- Grafana: http://172.29.215.125:3000 (admin/123456aA)"
echo "- Prometheus: http://172.29.215.125:9090"
echo "- Application: http://172.29.215.125:8086"
echo ""
echo "📋 Available dashboards:"
echo "- Application Monitoring Dashboard"
echo "- Alerts & Monitoring Dashboard"
echo ""
echo "🔔 Alert rules configured:"
echo "- ApplicationDown (Critical)"
echo "- HighCPUUsage (Warning)"
echo "- HighMemoryUsage (Warning)"
echo "- HighErrorRate (Warning)"
echo "- HighResponseTime (Warning)"
echo "- DatabaseConnectionIssues (Warning)" 