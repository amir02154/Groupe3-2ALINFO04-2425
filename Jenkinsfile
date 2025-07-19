pipeline {
    agent any

    tools {
 
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        SONARQUBE_SCANNER_HOME = tool 'sonar-scanner'
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'Amir-ben-othman-Foyer',
                    url: 'https://github.com/ahmedmensi/Groupe3-2ALINFO04-2425.git'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        /* stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Integration Tests') {
            steps {
                sh 'mvn verify -Pintegration-tests'
            }
        } */

        stage('Performance Test with JMeter') {
            steps {
                echo '🚀 Exécution des tests de performance JMeter...'
                sh '''
                    # Vérifier si le fichier de test existe
                    if [ ! -f "jmeter/test_plan.jmx" ]; then
                        echo "❌ Fichier de test JMeter non trouvé: jmeter/test_plan.jmx"
                        exit 1
                    fi
                    
                    # Vérifier que l'application Spring Boot est démarrée
                    echo "🔍 Vérification que l'application Spring Boot est accessible..."
                    MAX_ATTEMPTS=30
                    ATTEMPT=0
                    
                    while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
                        if curl -s http://localhost:8086/actuator/health > /dev/null 2>&1; then
                            echo "✅ Application Spring Boot accessible sur http://localhost:8086"
                            break
                        else
                            ATTEMPT=$((ATTEMPT + 1))
                            echo "⏳ Tentative $ATTEMPT/$MAX_ATTEMPTS - Application non accessible, attente..."
                            sleep 2
                        fi
                    done
                    
                    if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
                        echo "❌ Application Spring Boot non accessible après $MAX_ATTEMPTS tentatives"
                        echo "💡 Assurez-vous que l'application est démarrée sur le port 8086"
                        exit 1
                    fi
                    
                    # Installation de JMeter si nécessaire
                    echo "🔧 Vérification de l'installation de JMeter..."
                    if ! command -v jmeter &> /dev/null; then
                        echo "📦 Installation de JMeter..."
                        
                        # Télécharger et installer JMeter
                        JMETER_VERSION="5.6.3"
                        JMETER_DIR="/opt/jmeter"
                        
                        if [ ! -d "$JMETER_DIR" ]; then
                            echo "📥 Téléchargement de JMeter $JMETER_VERSION..."
                            sudo mkdir -p $JMETER_DIR
                            cd /tmp
                            wget -q https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-$JMETER_VERSION.tgz
                            sudo tar -xzf apache-jmeter-$JMETER_VERSION.tgz -C /opt/
                            sudo mv /opt/apache-jmeter-$JMETER_VERSION $JMETER_DIR
                            sudo ln -sf $JMETER_DIR/bin/jmeter /usr/local/bin/jmeter
                            sudo ln -sf $JMETER_DIR/bin/jmeter-server /usr/local/bin/jmeter-server
                            echo "✅ JMeter installé dans $JMETER_DIR"
                        fi
                        
                        # Vérifier l'installation
                        if [ -f "$JMETER_DIR/bin/jmeter" ]; then
                            JMETER_CMD="$JMETER_DIR/bin/jmeter"
                        else
                            echo "❌ Échec de l'installation de JMeter"
                            exit 1
                        fi
                    else
                        JMETER_CMD="jmeter"
                    fi
                    
                    echo "✅ Utilisation de JMeter: $JMETER_CMD"
                    echo "📊 Version JMeter: $($JMETER_CMD -v 2>&1 | head -1)"
                    
                    rm -rf jmeter/report
                    rm -f jmeter/results.jtl
                    mkdir -p jmeter/report
                    
                    echo "🎯 Test des endpoints: /actuator/health, /api/foyers, /api/etudiants"
                    $JMETER_CMD -n -t jmeter/test_plan.jmx -l jmeter/results.jtl -e -o jmeter/report
                    
                    # Vérifier si le rapport a été généré
                    if [ -f "jmeter/report/index.html" ]; then
                        echo "✅ Rapport JMeter généré avec succès"
                        echo "📊 Statistiques des tests:"
                        ls -la jmeter/report/
                        echo "📈 Graphiques disponibles dans le rapport HTML"
                    else
                        echo "❌ Échec de génération du rapport JMeter"
                        ls -la jmeter/
                    fi
                    
                    tail -n 20 jmeter/results.jtl || true
                '''
            }
        }

        stage('Publish JMeter Report in Jenkins') {
            steps {
                publishHTML(target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'jmeter/report',
                    reportFiles: 'index.html',
                    reportName: 'Performance Report'
                ])
            }
        }

        stage('Send Performance Report by Email') {
            steps {
                script {
                    emailext(
                        subject: "📊 Rapport JMeter - Build #${env.BUILD_NUMBER}",
                        body: """
                            <p>Bonjour,</p>
                            <p>Le test de performance JMeter s'est exécuté avec le statut : <b>${currentBuild.currentResult}</b></p>
                            <p><a href="${env.BUILD_URL}Performance_Report/">📈 Voir le rapport détaillé</a></p>
                            <p>Cordialement,</p>
                            <p>Votre Jenkins 🛠️</p>
                        """,
                        mimeType: 'text/html',
                        to: 'amir.benothman04@gmail.com',
                        attachmentsPattern: 'jmeter/report/**'
                    )
                }
            }
        }

        /* stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh 'mvn jacoco:report'
                        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }

        stage('Quality Gate (Non-Blocking)') {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    script {
                        try {
                            def qg = waitForQualityGate()
                            echo "Quality Gate status: ${qg.status}"
                            if (qg.status != 'OK') {
                                echo "⚠️ Quality Gate failed, but build will continue..."

                                withCredentials([
                                    string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                                    string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                                ]) {
                                    def message = "⚠️ *Quality Gate échoué*\n"
                                    message += "*Projet:* ${env.JOB_NAME}\n"
                                    message += "*Build:* [#${env.BUILD_NUMBER}](${env.BUILD_URL})\n"
                                    message += "*Statut:* ${qg.status}"

                                    sh """
                                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                                            -d chat_id=${CHAT_ID} \\
                                            -d parse_mode=Markdown \\
                                            -d text="${message}"
                                    """
                                }
                            }
                        } catch (err) {
                            echo "⚠️ Timeout ou erreur dans la récupération du Quality Gate"
                            echo err.toString()

                            withCredentials([
                                string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                                string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                            ]) {
                                def message = "⚠️ *Quality Gate timeout ou erreur*\n"
                                message += "*Projet:* ${env.JOB_NAME}\n"
                                message += "*Build:* [#${env.BUILD_NUMBER}](${env.BUILD_URL})\n"
                                message += "*Erreur:* ${err.toString()}"

                                sh """
                                    curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                                        -d chat_id=${CHAT_ID} \\
                                        -d parse_mode=Markdown \\
                                        -d text="${message}"
                                """
                            }
                        }
                    }
                }
            }
        } */

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        /* stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }

        stage('Download Artifact from Nexus') {
            steps {
                sh '''
                    ARTIFACT_VERSION=1.4.1-SNAPSHOT
                    ARTIFACT_ID=Groupe12Alinfo42425
                    GROUP_ID=tn.esprit.DevOps
                    REPO=maven-snapshots
                    ARTIFACT_NAME=${ARTIFACT_ID}-${ARTIFACT_VERSION}.jar
                    ARTIFACT_PATH=$(echo $GROUP_ID | tr '.' '/')/$ARTIFACT_ID/$ARTIFACT_VERSION/$ARTIFACT_NAME
                    NEXUS_URL=http://172.29.215.125:8081/repository/$REPO/$ARTIFACT_PATH
                    curl -u admin:123456aA -o $ARTIFACT_NAME $NEXUS_URL
                '''
            }
        } */

        /* stage('Build & Push Docker Image') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                    ]) {
                        sh '''
                            docker build -t "$DOCKER_USER/groupe12alinfo-app:1.4.1" --build-arg JAR_FILE=Groupe12Alinfo42425-1.4.1-SNAPSHOT.jar .
                            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                            docker push "$DOCKER_USER/groupe12alinfo-app:1.4.1"
                        '''
                    }
                }
            }
        }

        stage('Clean Old Containers') {
            steps {
                sh '''
                    docker rm -f lastfoyer-app-1 || true
                    docker rm -f lastfoyer-app-1 || true
                    docker rm -f foyer2-app-1 || true
                    docker rm -f foyer2-db-1 || true
                    docker rm -f final-app-1 || true
                    docker rm -f final-db-1 || true
                '''
            }
        }

        stage('Run with Docker Compose') {
            steps {
                sh '''
                    docker compose down || true
                    docker compose up -d
                '''
            }
        } */

        stage('Start Prometheus') {
            steps {
                script {
                    echo '🔍 Vérification du conteneur Prometheus...'
                    def result = sh(script: "docker inspect -f '{{.State.Running}}' prometheus-p || echo 'not-found'", returnStdout: true).trim()

                    if (result == 'true') {
                        echo '✅ Prometheus est déjà en cours.'
                    } else if (result == 'false') {
                        echo '🔁 Prometheus est arrêté. Démarrage...'
                        sh "docker start prometheus-p"
                    } else {
                        echo '🚀 Prometheus non trouvé. Lancement du conteneur...'
                        sh '''
                            docker run -d \
                              --name prometheus-p \
                              -p 9090:9090 \
                              -v "$PWD/prometheus:/etc/prometheus" \
                              prom/prometheus \
                              --config.file=/etc/prometheus/prometheus.yml
                        '''
                    }
                }
            }
        }

        stage('Start Grafana') {
            steps {
                script {
                    echo '🔍 Vérification du conteneur Grafana...'
                    def result = sh(script: "docker inspect -f '{{.State.Running}}' grafana-p || echo 'not-found'", returnStdout: true).trim()

                    if (result == 'true') {
                        echo '✅ Grafana est déjà en cours.'
                    } else if (result == 'false') {
                        echo '🔁 Grafana est arrêté. Démarrage...'
                        sh "docker start grafana-p"
                    } else {
                        echo '🚀 Grafana non trouvé. Lancement du conteneur...'
                        sh '''
                            docker run -d \
                              --name grafana-p \
                              -p 3000:3000 \
                              -e "GF_SECURITY_ADMIN_USER=admin" \
                              -e "GF_SECURITY_ADMIN_PASSWORD=123456aA" \
                              grafana/grafana
                        '''
                    }
                }
            }
        }

        stage('Import Jenkins Metrics Dashboard') {
            steps {
                sh '''
                    GRAFANA_URL="http://172.29.215.125:3000"
                    GRAFANA_USER="admin"
                    GRAFANA_PASS="123456aA"

                    if ! command -v jq &> /dev/null; then
                        echo "📦 Installation de jq..."
                        if command -v apt-get &> /dev/null; then
                            sudo apt-get update && sudo apt-get install -y jq
                        elif command -v yum &> /dev/null; then
                            sudo yum install -y jq
                        elif command -v dnf &> /dev/null; then
                            sudo dnf install -y jq
                        else
                            echo "❌ Impossible d'installer jq automatiquement"
                            exit 1
                        fi
                    fi

                    echo "✅ jq est disponible: $(jq --version)"

                    cp monitoring/grafana-dashboard-jenkins.json jenkins_metrics_dashboard.json
                    DASHBOARD_UID=$(jq -r '.uid' jenkins_metrics_dashboard.json)

                    EXISTS=$(curl -s -u $GRAFANA_USER:$GRAFANA_PASS "$GRAFANA_URL/api/dashboards/uid/$DASHBOARD_UID" | jq -r '.dashboard.uid // empty')

                    if [ "$EXISTS" = "$DASHBOARD_UID" ] && [ -n "$DASHBOARD_UID" ]; then
                        echo "Dashboard déjà existé"
                    else
                        jq -s '{
                            dashboard: .[0],
                            inputs: [{
                                name: "DS_PROMETHEUS",
                                type: "datasource",
                                pluginId: "prometheus",
                                value: "Prometheus"
                            }],
                            overwrite: true
                        }' jenkins_metrics_dashboard.json > payload_jenkins_dashboard_jenkins.json

                        curl -s -X POST $GRAFANA_URL/api/dashboards/import \
                            -H "Content-Type: application/json" \
                            -u $GRAFANA_USER:$GRAFANA_PASS \
                            -d @payload_jenkins_dashboard_jenkins.json
                    fi
                '''
            }
        }

        stage('Import Dashboard Grafana') {
            steps {
                sh '''
                    DASHBOARD_UID="9964"
                    GRAFANA_URL="http://172.29.215.125:3000"
                    GRAFANA_USER="admin"
                    GRAFANA_PASS="123456aA"

                    if ! command -v jq &> /dev/null; then
                        echo "📦 Installation de jq..."
                        if command -v apt-get &> /dev/null; then
                            sudo apt-get update && sudo apt-get install -y jq
                        elif command -v yum &> /dev/null; then
                            sudo yum install -y jq
                        elif command -v dnf &> /dev/null; then
                            sudo dnf install -y jq
                        else
                            echo "❌ Impossible d'installer jq automatiquement"
                            exit 1
                        fi
                    fi

                    echo "✅ jq est disponible: $(jq --version)"

                    EXISTS=$(curl -s -u $GRAFANA_USER:$GRAFANA_PASS "$GRAFANA_URL/api/dashboards/uid/$DASHBOARD_UID" | jq -r '.dashboard.uid // empty')

                    if [ "$EXISTS" = "$DASHBOARD_UID" ]; then
                        echo "Dashboard déjà existé"
                    else
                        curl -s https://grafana.com/api/dashboards/9964/revisions/1/download -o node_exporter_dashboard.json

                        jq -s '{
                            dashboard: .[0],
                            inputs: [{
                                name: "DS_PROMETHEUS",
                                type: "datasource",
                                pluginId: "prometheus",
                                value: "Prometheus"
                            }],
                            overwrite: true
                        }' node_exporter_dashboard.json > payload_dashboard_9964.json

                        curl -s -X POST $GRAFANA_URL/api/dashboards/import \
                            -H "Content-Type: application/json" \
                            -u $GRAFANA_USER:$GRAFANA_PASS \
                            -d @payload_dashboard_9964.json
                    fi
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'jmeter/results.jtl', allowEmptyArchive: true
            archiveArtifacts artifacts: 'jmeter/report/**', allowEmptyArchive: true

            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'jmeter/report',
                reportFiles: 'index.html',
                reportName: 'JMeter Performance Report'
            ])

            script {
                jacoco execPattern: '**/target/jacoco.exec'

                def status = currentBuild.currentResult
                def emoji = status == 'SUCCESS' ? '✅' : (status == 'FAILURE' ? '❌' : '⚠️')
                def message = "${emoji} *Pipeline Jenkins Terminé*%0A"
                message += "*Projet:* ${env.JOB_NAME}%0A"
                message += "*Build:* [#${env.BUILD_NUMBER}](${env.BUILD_URL})%0A"
                message += "*Statut:* ${status}"

                withCredentials([
                    string(credentialsId: 'TELEGRAM_BOT_TOKEN', variable: 'BOT_TOKEN'),
                    string(credentialsId: 'TELEGRAM_CHAT_ID', variable: 'CHAT_ID')
                ]) {
                    sh """
                        curl -s -X POST https://api.telegram.org/bot${BOT_TOKEN}/sendMessage \\
                            -d chat_id=${CHAT_ID} \\
                            -d parse_mode=Markdown \\
                            -d text="${message}"
                    """
                }
            }
        }
    }
}
