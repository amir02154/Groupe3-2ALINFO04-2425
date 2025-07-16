# Monitoring Setup for Foyer Application

## Overview

This project includes a comprehensive monitoring setup with Prometheus, Grafana, and AlertManager for the Foyer Spring Boot application.

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

## Components

### 1. Prometheus
- **Port**: 9090
- **Configuration**: `prometheus/prometheus.yml`
- **Alert Rules**: `prometheus/alert_rules.yml`
- **Purpose**: Collects metrics from the application and database

### 2. Grafana
- **Port**: 3000
- **Credentials**: admin/123456aA
- **Dashboards**: 
  - Application Monitoring Dashboard
  - Alerts & Monitoring Dashboard
- **Alert Rules**: Configured via API

### 3. AlertManager
- **Port**: 9093
- **Configuration**: `alertmanager/alertmanager.yml`
- **Purpose**: Manages alert notifications

## Jenkins Pipeline Stages

The enhanced Jenkins pipeline includes the following monitoring-related stages:

1. **Setup Monitoring Infrastructure**: Creates necessary directories and copies configurations
2. **Deploy with Docker Compose**: Starts all services
3. **Wait for Services to Start**: Ensures all services are ready
4. **Configure Prometheus Data Source**: Sets up Prometheus as Grafana datasource
5. **Import Grafana Dashboards**: Imports monitoring dashboards
6. **Configure Grafana Alert Rules**: Sets up alert rules and notification channels
7. **Verify Monitoring Setup**: Validates the entire monitoring stack
8. **Health Check**: Performs final health checks

## Alert Rules

### Prometheus Alerts
- **ApplicationDown**: Application is not responding
- **HighCPUUsage**: CPU usage > 80%
- **HighMemoryUsage**: Memory usage > 85%
- **HighErrorRate**: Error rate > 10%
- **HighResponseTime**: 95th percentile response time > 2s
- **DatabaseConnectionIssues**: Connection pool usage > 80%

### Grafana Alerts
- High CPU Usage
- High Memory Usage
- Application Down
- High Error Rate
- High Response Time

## Dashboards

### 1. Application Monitoring Dashboard
- CPU Usage
- Memory Usage
- HTTP Requests Rate
- Response Time (95th percentile)
- JVM Heap Memory
- Database Connections
- Error Rate
- Application Status

### 2. Alerts & Monitoring Dashboard
- Alert Status Overview
- Active Alerts by Severity
- Application Health Score
- Error Rate Trend
- System Resources Alert Status
- Response Time Alert Status
- Database Health

## URLs

- **Grafana**: http://localhost:3000 (admin/123456aA)
- **Prometheus**: http://localhost:9090
- **AlertManager**: http://localhost:9093
- **Application**: http://localhost:8086

## Testing

Run the monitoring test script:
```bash
chmod +x scripts/test-monitoring.sh
./scripts/test-monitoring.sh
```

## Configuration Files

### Prometheus
- `prometheus/prometheus.yml`: Main configuration
- `prometheus/alert_rules.yml`: Alert rules

### Grafana
- `grafana/provisioning/datasources/prometheus.yml`: Prometheus datasource
- `grafana/provisioning/dashboards/dashboard.yml`: Dashboard provisioning
- `grafana/provisioning/alerting/alerting.yml`: Alert provisioning
- `grafana/dashboards/alerts-dashboard.json`: Alerts dashboard

### AlertManager
- `alertmanager/alertmanager.yml`: Alert routing and notifications

## Troubleshooting

### Common Issues

1. **Prometheus targets down**:
   - Check if application is running
   - Verify metrics endpoint `/actuator/prometheus`
   - Check network connectivity

2. **Grafana can't connect to Prometheus**:
   - Verify Prometheus is running on port 9090
   - Check datasource configuration
   - Ensure network connectivity between containers

3. **Alerts not firing**:
   - Check alert rules syntax
   - Verify metrics are being collected
   - Check AlertManager configuration

### Logs

```bash
# Check Prometheus logs
docker logs prometheus

# Check Grafana logs
docker logs grafana

# Check AlertManager logs
docker logs alertmanager

# Check application logs
docker logs app
```

## Customization

### Adding New Metrics

1. Add metrics to your Spring Boot application
2. Update Prometheus configuration if needed
3. Create new Grafana panels
4. Add alert rules if required

### Adding New Alerts

1. Add alert rule to `prometheus/alert_rules.yml`
2. Reload Prometheus configuration
3. Create corresponding Grafana alert if needed

### Adding New Dashboards

1. Create dashboard JSON file
2. Place in `grafana/dashboards/`
3. Dashboard will be auto-provisioned

## Security Notes

- Default credentials are used for demonstration
- In production, use strong passwords
- Consider using secrets management
- Enable HTTPS for all services
- Restrict network access as needed 