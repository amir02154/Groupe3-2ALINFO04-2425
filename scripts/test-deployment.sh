#!/bin/bash

echo "🧪 Testing Nexus Deployment..."

# Configuration
NEXUS_URL="http://172.29.215.125:8081"
REPO_URL="$NEXUS_URL/repository/maven-releases"
ARTIFACT_PATH="tn/esprit/spring/Foyer/1.4.0"

# Test if Nexus is accessible
echo "Testing Nexus connectivity..."
if curl -s "$NEXUS_URL" > /dev/null; then
    echo "✅ Nexus is accessible"
else
    echo "❌ Cannot connect to Nexus"
    exit 1
fi

# Test if artifact exists
echo "Testing artifact availability..."
if curl -s "$REPO_URL/$ARTIFACT_PATH/Foyer-1.4.0.jar" > /dev/null; then
    echo "✅ Artifact exists in Nexus"
else
    echo "❌ Artifact not found in Nexus"
    echo "Expected URL: $REPO_URL/$ARTIFACT_PATH/Foyer-1.4.0.jar"
fi

# Test if POM exists
if curl -s "$REPO_URL/$ARTIFACT_PATH/Foyer-1.4.0.pom" > /dev/null; then
    echo "✅ POM exists in Nexus"
else
    echo "❌ POM not found in Nexus"
fi

echo "🎯 Deployment test completed!" 