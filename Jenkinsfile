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
                script {
                    // Vérifier que SonarQube est accessible depuis WSL
                    sh 'curl -f http://192.168.228.141:9000/api/system/status || echo "SonarQube not accessible"'
                    
                    // Exécuter l'analyse SonarQube
                    sh 'mvn test jacoco:report'
                    sh "mvn sonar:sonar -Dsonar.host.url=http://192.168.228.141:9000 -Dsonar.login=${SONAR_TOKEN}"
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
                    
                    // Attendre que la base de données soit prête
                    sh 'sleep 30'
                    
                    // Attendre que l'application soit prête
                    sh 'sleep 30'
                    
                    // Vérifier que l'application répond
                    sh 'curl -f http://localhost:8081/actuator/health || exit 1'
                }
            }
        }

        stage('Application Setup') {
            steps {
                script {
                    echo 'Configuration de l\'application...'
                    
                    // Vérifier que la base de données est accessible
                    sh 'sleep 10'
                    sh 'docker exec pipeline-db-1 mysql -uroot -proot -e "SELECT 1;" || echo "Database not ready yet"'
                    
                    // Vérifier que l'application répond
                    sh 'sleep 10'
                    sh 'curl -f http://localhost:8081/actuator/health || echo "Application not ready yet"'
                    
                    echo 'Application setup completed'
                    echo 'Application: http://localhost:8081'
                    echo 'Database: localhost:3306 (root/root)'
                    echo 'SonarQube: http://192.168.228.141:9000 (admin/admin)'
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
            echo 'Application accessible sur: http://localhost:8081'
        }
        
        failure {
            echo 'Pipeline a échoué!'
        }
    }
} 