# Pipeline Docker pour l'Application Foyer

Ce projet inclut un pipeline Jenkins complet avec Docker, tests d'intégration, et monitoring.

## Prérequis

1. **Jenkins** avec les plugins suivants :
   - Docker Pipeline
   - SonarQube Scanner
   - Credentials Binding

2. **Credentials Jenkins** à configurer :
   - `sonar-token` : Token SonarQube
   - `docker-hub-password` : Mot de passe Docker Hub (14771938Aze-)

3. **Docker** installé sur le serveur Jenkins

## Configuration Jenkins

### 1. Outils à configurer dans Jenkins :
- `M2_HOME` : Chemin vers Maven
- `JAVA_HOME` : Chemin vers JDK 17
- `sonar-scanner` : Chemin vers SonarQube Scanner

### 2. Credentials à ajouter :
- **sonar-token** (Secret text) : Token SonarQube
- **docker-hub-password** (Secret text) : 14771938Aze-

## Pipeline Stages

1. **Checkout** : Récupération du code depuis GitHub
2. **Clean** : Nettoyage du projet Maven
3. **Compile** : Compilation du code
4. **SonarQube Analysis** : Analyse de qualité du code
5. **Unit Tests** : Exécution des tests unitaires
6. **Integration Tests** : Exécution des tests d'intégration
7. **Package** : Création du JAR
8. **Deploy to Nexus** : Déploiement sur Nexus
9. **Build Docker Image** : Construction de l'image Docker
10. **Push to Docker Hub** : Publication sur Docker Hub
11. **Deploy with Docker Compose** : Déploiement avec docker-compose
12. **Monitoring Setup** : Configuration du monitoring

## Services Docker

Le `docker-compose.yml` inclut :

- **foyer-app** : Application Spring Boot (port 8080)
- **nexus** : Repository Nexus (port 8081)
- **prometheus** : Monitoring Prometheus (port 9090)
- **grafana** : Dashboard Grafana (port 3000)

## Accès aux Services

Après le déploiement :

- **Application** : http://localhost:8080
- **Nexus** : http://localhost:8081
- **Prometheus** : http://localhost:9090
- **Grafana** : http://localhost:3000 (admin/admin)

## Image Docker

L'image sera publiée sur Docker Hub sous le nom :
`ahmedmenssi624/foyer-app:latest`

## Tests d'Intégration

Les tests d'intégration doivent être nommés avec le suffixe `IT.java` et placés dans le répertoire `src/test/java`.

## Monitoring

L'application expose des métriques Prometheus sur `/actuator/prometheus` et un endpoint de santé sur `/actuator/health`.

## Commandes Utiles

```bash
# Construire l'image localement
docker build -t foyer-app .

# Démarrer les services
docker-compose up -d

# Arrêter les services
docker-compose down

# Voir les logs
docker-compose logs -f foyer-app
```

## Troubleshooting

1. **Erreur de connexion Docker Hub** : Vérifier les credentials Jenkins
2. **Ports déjà utilisés** : Arrêter les conteneurs existants avec `docker-compose down`
3. **Tests d'intégration échouent** : Vérifier la configuration de la base de données de test 