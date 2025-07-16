# Corrections pour les Problèmes de Déploiement

## Problèmes Identifiés et Solutions

### 1. Erreur de Compilation - Conflits Git

**Problème :**
```
[ERROR] class, interface, enum, or record expected
```

**Cause :**
- Marqueurs de conflit Git (`<<<<<<< HEAD`, `=======`, `>>>>>>> 2a5493e616567e2fa2bf40fd8ed126611f7d04c1`) dans `FoyerAspect4SIM2.java`

**Solution :**
- Suppression des marqueurs de conflit Git
- Conservation des imports corrects pour les annotations AOP

### 2. Erreur de Déploiement Nexus - Version SNAPSHOT

**Problème :**
```
[ERROR] Failed to deploy artifacts: Could not transfer artifact tn.esprit.spring:Foyer:pom:1.4.0-20250716.231355-1 from/to deploymentRepo
status: 400 Bad Request
```

**Cause :**
- Version SNAPSHOT (`1.4.0-SNAPSHOT`) tentant de se déployer vers le repository `maven-releases`
- Les repositories de releases n'acceptent pas les versions SNAPSHOT

**Solution :**
- Changement de la version de `1.4.0-SNAPSHOT` vers `1.4.0` (version release)
- Configuration des repositories SNAPSHOT et RELEASE dans `pom.xml`

## Modifications Apportées

### 1. Fichier `pom.xml`

```xml
<!-- Avant -->
<version>1.4.0-SNAPSHOT</version>

<!-- Après -->
<version>1.4.0</version>

<!-- Ajout de la configuration des repositories -->
<distributionManagement>
    <repository>
        <id>deploymentRepo</id>
        <url>http://172.29.215.125:8081/repository/maven-releases/</url>
    </repository>
    <snapshotRepository>
        <id>deploymentRepo</id>
        <url>http://172.29.215.125:8081/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

### 2. Fichier `FoyerAspect4SIM2.java`

```java
// Avant (avec conflits Git)
<<<<<<< HEAD
import org.aspectj.lang.annotation.*;
=======
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
>>>>>>> 2a5493e616567e2fa2bf40fd8ed126611f7d04c1

// Après (corrigé)
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
```

### 3. Configuration Maven (`maven-settings.xml`)

- Création d'un fichier de configuration Maven avec les credentials Nexus
- Configuration des mirrors et repositories
- Support des variables d'environnement pour les credentials

### 4. Pipeline Jenkins (`Jenkinsfile`)

- Mise à jour de la version dans l'URL de téléchargement
- Ajout de la configuration Maven avec credentials
- Amélioration des stages de monitoring

## Nouveaux Fichiers Créés

1. **`maven-settings.xml`** : Configuration Maven pour Nexus
2. **`scripts/test-deployment.sh`** : Script de test du déploiement
3. **`DEPLOYMENT_FIXES.md`** : Ce document

## Tests Recommandés

### 1. Test de Compilation
```bash
mvn clean compile
```

### 2. Test de Déploiement
```bash
# Avec credentials
mvn deploy -DskipTests --settings maven-settings.xml
```

### 3. Test de Monitoring
```bash
./scripts/test-monitoring.sh
```

### 4. Test de Déploiement
```bash
./scripts/test-deployment.sh
```

## URLs de Monitoring

- **Grafana** : http://localhost:3000 (admin/123456aA)
- **Prometheus** : http://localhost:9090
- **AlertManager** : http://localhost:9093
- **Application** : http://localhost:8086

## Prochaines Étapes

1. **Tester le pipeline Jenkins** avec les nouvelles configurations
2. **Vérifier les credentials Nexus** dans Jenkins
3. **Tester le déploiement** vers Nexus
4. **Vérifier le monitoring** après déploiement

## Notes Importantes

- Les versions SNAPSHOT ne peuvent pas être déployées vers `maven-releases`
- Utilisez `maven-snapshots` pour les versions SNAPSHOT
- Utilisez `maven-releases` pour les versions stables
- Les credentials Nexus doivent être configurés dans Jenkins 