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
        NEXUS_USER = credentials('nexus-username')
        NEXUS_PASS = credentials('nexus-password')
        NEXUS_URL = 'http://192.168.228.141:8081/'
        GROUP_ID = 'tn/esprit/DevOps'
        ARTIFACT_ID = 'Groupe12Alinfo42425'
        VERSION = '1.4.1-SNAPSHOT'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'ahmed-mensi-etudiant',
                    url: 'https://github.com/ahmedmensi/Groupe3-2ALINFO04-2425.git'
            }
        }

        stage('Unit & Integration Tests') {
            steps {
                sh 'mvn test'
                sh 'mvn verify -DskipUnitTests=false'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    sh 'mvn jacoco:report'
                    sh "mvn sonar:sonar -Dsonar.host.url=http://192.168.228.141:9000 -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }

        stage('Download JAR from Nexus') {
            steps {
                sh '''
                wget --user=${NEXUS_USER} --password=${NEXUS_PASS} \
                "${NEXUS_URL}/repository/maven-snapshots/${GROUP_ID}/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${VERSION}.jar" \
                -O app.jar
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ."
                sh "docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_IMAGE_NAME}:latest"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh "echo '${DOCKER_PASSWORD}' | docker login -u ${DOCKER_USERNAME} --password-stdin"
                sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}"
                sh "docker push ${DOCKER_IMAGE_NAME}:latest"
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