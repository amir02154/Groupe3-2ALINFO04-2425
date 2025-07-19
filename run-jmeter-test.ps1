# Script PowerShell pour exécuter les tests JMeter
Write-Host "🚀 Démarrage des tests de performance JMeter..." -ForegroundColor Green

# Vérifier si JMeter est installé
$jmeterPath = Get-Command jmeter -ErrorAction SilentlyContinue
if (-not $jmeterPath) {
    Write-Host "📦 JMeter non trouvé, installation en cours..." -ForegroundColor Yellow
    
    # Créer le répertoire JMeter
    $jmeterDir = "C:\jmeter"
    if (-not (Test-Path $jmeterDir)) {
        New-Item -ItemType Directory -Path $jmeterDir -Force
    }
    
    # Télécharger JMeter
    $jmeterVersion = "5.6.3"
    $jmeterUrl = "https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-$jmeterVersion.zip"
    $jmeterZip = "$env:TEMP\apache-jmeter-$jmeterVersion.zip"
    
    Write-Host "📥 Téléchargement de JMeter $jmeterVersion..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $jmeterUrl -OutFile $jmeterZip
    
    # Extraire JMeter
    Write-Host "📦 Extraction de JMeter..." -ForegroundColor Yellow
    Expand-Archive -Path $jmeterZip -DestinationPath $env:TEMP -Force
    Move-Item -Path "$env:TEMP\apache-jmeter-$jmeterVersion" -Destination $jmeterDir -Force
    
    # Ajouter JMeter au PATH
    $jmeterBin = "$jmeterDir\bin"
    $env:PATH += ";$jmeterBin"
    
    Write-Host "✅ JMeter installé dans $jmeterDir" -ForegroundColor Green
} else {
    Write-Host "✅ JMeter trouvé: $($jmeterPath.Source)" -ForegroundColor Green
}

# Vérifier que l'application Spring Boot est démarrée
Write-Host "🔍 Vérification que l'application Spring Boot est accessible..." -ForegroundColor Yellow
$maxAttempts = 30
$attempt = 0

do {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8086/actuator/health" -TimeoutSec 5 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ Application Spring Boot accessible sur http://localhost:8086" -ForegroundColor Green
            break
        }
    } catch {
        $attempt++
        Write-Host "⏳ Tentative $attempt/$maxAttempts - Application non accessible, attente..." -ForegroundColor Yellow
        Start-Sleep -Seconds 2
    }
} while ($attempt -lt $maxAttempts)

if ($attempt -eq $maxAttempts) {
    Write-Host "❌ Application Spring Boot non accessible après $maxAttempts tentatives" -ForegroundColor Red
    Write-Host "💡 Assurez-vous que l'application est démarrée sur le port 8086" -ForegroundColor Red
    exit 1
}

# Préparer les répertoires
if (Test-Path "jmeter\report") {
    Remove-Item -Path "jmeter\report" -Recurse -Force
}
if (Test-Path "jmeter\results.jtl") {
    Remove-Item -Path "jmeter\results.jtl" -Force
}
New-Item -ItemType Directory -Path "jmeter\report" -Force | Out-Null

# Exécuter JMeter
Write-Host "🎯 Test des endpoints: /actuator/health, /api/foyers, /api/etudiants" -ForegroundColor Cyan
Write-Host "📊 Exécution des tests de performance..." -ForegroundColor Cyan

$jmeterCmd = "jmeter"
if (-not (Get-Command $jmeterCmd -ErrorAction SilentlyContinue)) {
    $jmeterCmd = "C:\jmeter\bin\jmeter.bat"
}

& $jmeterCmd -n -t "jmeter\test_plan.jmx" -l "jmeter\results.jtl" -e -o "jmeter\report"

# Vérifier le rapport
if (Test-Path "jmeter\report\index.html") {
    Write-Host "✅ Rapport JMeter généré avec succès" -ForegroundColor Green
    Write-Host "📊 Statistiques des tests:" -ForegroundColor Cyan
    Get-ChildItem "jmeter\report" | ForEach-Object { Write-Host "  - $($_.Name)" -ForegroundColor White }
    Write-Host "📈 Graphiques disponibles dans le rapport HTML" -ForegroundColor Cyan
    Write-Host "🌐 Ouvrir le rapport: jmeter\report\index.html" -ForegroundColor Green
} else {
    Write-Host "❌ Échec de génération du rapport JMeter" -ForegroundColor Red
    Get-ChildItem "jmeter" | ForEach-Object { Write-Host "  - $($_.Name)" -ForegroundColor White }
}

# Afficher les dernières lignes du fichier de résultats
if (Test-Path "jmeter\results.jtl") {
    Write-Host "📋 Dernières lignes du fichier de résultats:" -ForegroundColor Cyan
    Get-Content "jmeter\results.jtl" -Tail 10 | ForEach-Object { Write-Host "  $_" -ForegroundColor Gray }
}

Write-Host "✅ Tests JMeter terminés!" -ForegroundColor Green 