#!/bin/bash

echo "🔧 Installation de JMeter sur le serveur Jenkins..."

# Vérifier si JMeter est déjà installé
if command -v jmeter &> /dev/null; then
    echo "✅ JMeter est déjà installé: $(jmeter -v 2>&1 | head -1)"
    exit 0
fi

# Variables
JMETER_VERSION="5.6.3"
JMETER_DIR="/opt/jmeter"
JMETER_ARCHIVE="apache-jmeter-$JMETER_VERSION.tgz"
JMETER_URL="https://archive.apache.org/dist/jmeter/binaries/$JMETER_ARCHIVE"

echo "📥 Téléchargement de JMeter $JMETER_VERSION..."

# Créer le répertoire d'installation
sudo mkdir -p $JMETER_DIR

# Télécharger JMeter
cd /tmp
wget -q $JMETER_URL

if [ $? -ne 0 ]; then
    echo "❌ Échec du téléchargement de JMeter"
    exit 1
fi

echo "📦 Extraction de JMeter..."
sudo tar -xzf $JMETER_ARCHIVE -C /opt/

# Déplacer vers le répertoire final
sudo mv /opt/apache-jmeter-$JMETER_VERSION $JMETER_DIR

# Créer les liens symboliques
sudo ln -sf $JMETER_DIR/bin/jmeter /usr/local/bin/jmeter
sudo ln -sf $JMETER_DIR/bin/jmeter-server /usr/local/bin/jmeter-server

# Vérifier l'installation
if [ -f "$JMETER_DIR/bin/jmeter" ]; then
    echo "✅ JMeter installé avec succès dans $JMETER_DIR"
    echo "📊 Version: $($JMETER_DIR/bin/jmeter -v 2>&1 | head -1)"
    
    # Nettoyer
    rm -f /tmp/$JMETER_ARCHIVE
    
    echo "🎯 JMeter est maintenant disponible via la commande 'jmeter'"
else
    echo "❌ Échec de l'installation de JMeter"
    exit 1
fi 