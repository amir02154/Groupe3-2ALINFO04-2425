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

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Integration Tests') {
            steps {
                sh 'mvn verify -Pintegration-tests'
            }
        }

        stage('Performance Test with JMeter') {
            steps {
                echo '🚀 Exécution des tests de performance JMeter...'
                sh '''
                    rm -rf jmeter/report
                    rm -f jmeter/results.jtl
                    mkdir -p jmeter/report
                    /opt/jmeter/bin/jmeter -n -t jmeter/performance-test-demo.jmx -l jmeter/results.jtl -e -o jmeter/report
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
                        to: 'amir.benothman154@gmail.com',
                        attachmentsPattern: 'jmeter/report/**'
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
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
        }

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
*/


        stage('Build & Push Docker Image') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                    ]) {
                        sh '''
                            docker build -t "$DOCKER_USER/groupe12alinfo-app:1.4.1" .
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
                    docker rm -f foyer1-app-1 || true
                    docker rm -f foyer1-db-1 || true
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
        }

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

        stage('Create Jenkins Alert') {
            steps {
                sh '''
                    curl -s -X POST http://172.29.215.125:3000/api/v1/provisioning/alert-rules \
                        -H "Content-Type: application/json" \
                        -u admin:123456aA \
                        -d @monitoring/grafana-alert-rule-jenkins.json
                '''
            }
        }

        stage('Import Jenkins Dashboard Grafana') {
            steps {
                sh '''
                    curl -s -X POST http://172.29.215.125:3000/api/dashboards/db \
                        -H "Content-Type: application/json" \
                        -u admin:123456aA \
                        -d @monitoring/grafana-dashboard-jenkins.json
                '''
            }
        }

        stage('Import Dashboard Grafana') {
            steps {
                sh '''
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

                    curl -s -X POST http://172.29.215.125:3000/api/dashboards/import \
                        -H "Content-Type: application/json" \
                        -u admin:123456aA \
                        -d @payload_dashboard_9964.json
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'jmeter/results.jtl', allowEmptyArchive: true
            archiveArtifacts artifacts: 'jmeter/report/**', allowEmptyArchive: true

            jacoco execPattern: '**/target/jacoco.exec'

            script {
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
