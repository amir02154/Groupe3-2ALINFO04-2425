#!/bin/bash

echo "🧹 Cleaning Maven cache and rebuilding..."

# Clean Maven cache
echo "Cleaning Maven local repository cache..."
rm -rf ~/.m2/repository/tn/esprit/spring/Foyer/

# Clean project
echo "Cleaning project..."
mvn clean

# Verify version in pom.xml
echo "Verifying version in pom.xml..."
VERSION=$(grep -o '<version>.*</version>' pom.xml | head -1 | sed 's/<version>\(.*\)<\/version>/\1/')
echo "Current version: $VERSION"

if [[ "$VERSION" == *"SNAPSHOT"* ]]; then
    echo "❌ ERROR: Version still contains SNAPSHOT: $VERSION"
    echo "Please update pom.xml to use a release version (e.g., 1.4.0)"
    exit 1
fi

# Compile
echo "Compiling project..."
mvn compile

# Package
echo "Packaging project..."
mvn package -DskipTests

# Deploy with explicit settings
echo "Deploying to Nexus..."
if [ -n "$NEXUS_USER" ] && [ -n "$NEXUS_PASS" ]; then
    echo "Using credentials from environment variables..."
    sed "s/\${NEXUS_USER}/$NEXUS_USER/g; s/\${NEXUS_PASS}/$NEXUS_PASS/g" maven-settings.xml > settings.xml
    mvn deploy -DskipTests --settings settings.xml
else
    echo "No credentials found, trying without settings..."
    mvn deploy -DskipTests
fi

echo "✅ Clean and deploy completed!" 