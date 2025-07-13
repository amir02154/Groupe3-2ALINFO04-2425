# Guide de Configuration du Pipeline Jenkins

## Étape 1 : Créer un nouveau Pipeline Job

1. **Dans Jenkins, cliquez sur "New Item"**
2. **Entrez un nom pour votre job** (ex: "Foyer-App-Pipeline")
3. **Sélectionnez "Pipeline"**
4. **Cliquez sur "OK"**

## Étape 2 : Configuration du Pipeline

### Option A : Pipeline depuis SCM (Recommandé)

1. **Dans la section "Pipeline"** :
   - Definition : `Pipeline script from SCM`
   - SCM : `Git`
   - Repository URL : `https://github.com/ahmedmensi/Groupe3-2ALINFO04-2425.git`
   - Credentials : Ajoutez vos credentials GitHub si nécessaire
   - Branch Specifier : `*/Soumaya-Arbi-reservation`
   - Script Path : `Jenkinsfile`

2. **Cliquez sur "Save"**

### Option B : Pipeline Script Direct

1. **Dans la section "Pipeline"** :
   - Definition : `Pipeline script`
   - Copiez le contenu du Jenkinsfile dans la zone de texte

## Étape 3 : Lancer la Build

### Méthode 1 : Interface Web
1. **Retournez à la page principale du job**
2. **Cliquez sur "Build Now"** (bouton bleu)

### Méthode 2 : Webhook GitHub (Automatique)
1. **Dans votre repository GitHub** :
   - Allez dans Settings → Webhooks
   - Add webhook
   - Payload URL : `http://votre-jenkins-url/github-webhook/`
   - Content type : `application/json`
   - Sélectionnez les événements (push, pull request)

### Méthode 3 : URL de Build
```
http://votre-jenkins-url/job/Foyer-App-Pipeline/build
```

## Étape 4 : Suivre la Build

1. **Cliquez sur le numéro de build** dans la liste
2. **Cliquez sur "Console Output"** pour voir les logs en temps réel
3. **Ou utilisez "Blue Ocean"** pour une vue graphique

## Étape 5 : Vérification des Services

Après une build réussie, vérifiez que les services sont accessibles :

```bash
# Application
curl http://localhost:8080/actuator/health

# Prometheus
curl http://localhost:9090/-/healthy

# Grafana
curl http://localhost:3000/api/health

# Nexus
curl http://localhost:8081/service/rest/v1/status
```

## Commandes Utiles

### Voir les logs des conteneurs
```bash
docker-compose logs -f foyer-app
docker-compose logs -f prometheus
docker-compose logs -f grafana
```

### Arrêter les services
```bash
docker-compose down
```

### Redémarrer les services
```bash
docker-compose restart
```

## Troubleshooting

### Erreur de credentials
- Vérifiez que les credentials `sonar-token` et `docker-hub-password` sont bien configurés
- Vérifiez que les IDs correspondent exactement à ceux du Jenkinsfile

### Erreur de ports
```bash
# Vérifier les ports utilisés
netstat -tulpn | grep :8080
netstat -tulpn | grep :3000
netstat -tulpn | grep :9090

# Arrêter les conteneurs existants
docker-compose down
docker system prune -f
```

### Erreur de Docker
```bash
# Vérifier que Docker est installé et fonctionne
docker --version
docker-compose --version

# Vérifier les permissions Docker
sudo usermod -aG docker jenkins
```

### Erreur de Maven
- Vérifiez que Maven est installé et configuré
- Vérifiez que le `pom.xml` est valide
- Vérifiez les dépendances réseau

## Monitoring de la Build

### Variables d'environnement disponibles
- `${env.BUILD_NUMBER}` : Numéro de build
- `${env.BUILD_URL}` : URL de la build
- `${env.GIT_COMMIT}` : Commit Git
- `${env.GIT_BRANCH}` : Branche Git

### Notifications (Optionnel)
Ajoutez dans le Jenkinsfile pour des notifications Slack/Email :

```groovy
post {
    success {
        echo 'Build réussie! Envoi de notification...'
        // Ajoutez ici votre logique de notification
    }
    failure {
        echo 'Build échouée! Envoi de notification...'
        // Ajoutez ici votre logique de notification
    }
}
``` 