pipeline {
    agent any

    tools {
        maven 'M2_HOME'
        jdk 'JAVA_HOME'
    }

    environment {
        SONARQUBE_SCANNER_HOME = tool 'sonar-scanner'
        SONAR_TOKEN = credentials('sonar-token')
        DOCKER_IMAGE_NAME = 'ahmedmenssi624/foyer-app'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        DOCKER_USERNAME = 'ahmedmenssi624@gmail.com'
        DOCKER_PASSWORD = credentials('docker-hub-password')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'ahmed-mensi-etudiant',
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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn test jacoco:report'
                    sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Integration Tests') {
            steps {
                sh 'mvn verify -DskipUnitTests'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Déploiement sur Nexus...'
                sh 'mvn deploy -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Trouver le nom du fichier JAR généré
                    def jarFile = sh(
                        script: 'find target -name "*.jar" -type f | head -1',
                        returnStdout: true
                    ).trim()
                    
                    echo "JAR file found: ${jarFile}"
                    
                    // Construire l'image Docker
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    // Se connecter à Docker Hub
                    sh "echo '${DOCKER_PASSWORD}' | docker login -u ${DOCKER_USERNAME} --password-stdin"
                    
                    // Pousser l'image
                    sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    // Arrêter tous les conteneurs existants
                    sh 'docker compose down || true'
                    sh 'docker stop $(docker ps -q) || true'
                    sh 'docker rm $(docker ps -aq) || true'
                    
                    // Attendre un peu pour libérer les ports
                    sh 'sleep 5'
                    
                    // Démarrer les services avec docker-compose
                    sh 'docker compose up -d'
                    
                    // Attendre que l'application soit prête
                    sh 'sleep 30'
                    
                    // Vérifier que l'application répond
                    sh 'curl -f http://localhost:8080/actuator/health || exit 1'
                }
            }
        }

        stage('Monitoring Setup') {
            steps {
                script {
                    echo 'Configuration de Prometheus et Grafana...'
                    
                    // Vérifier que Prometheus est accessible
                    sh 'sleep 10'
                    sh 'curl -f http://localhost:9090/-/healthy || echo "Prometheus not ready yet"'
                    
                    // Vérifier que Grafana est accessible
                    sh 'sleep 10'
                    sh 'curl -f http://localhost:3000/api/health || echo "Grafana not ready yet"'
                    
                    echo 'Monitoring setup completed'
                    echo 'Prometheus: http://localhost:9090'
                    echo 'Grafana: http://localhost:3000 (admin/admin)'
                    echo 'Nexus: http://localhost:8081'
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    echo 'Nettoyage des ressources...'
                    // Nettoyer les images Docker locales (optionnel)
                    sh 'docker image prune -f || true'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé. Nettoyage ou notifications possibles ici.'
        }
        
        success {
            echo 'Pipeline exécuté avec succès!'
            echo "Image Docker: ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
            echo 'Application accessible sur: http://localhost:8080'
        }
        
        failure {
            echo 'Pipeline a échoué!'
        }
    }
} 