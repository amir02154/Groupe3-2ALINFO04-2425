# Script d'exécution des tests JMeter avec rapports enrichis
Write-Host "🚀 Démarrage des tests de performance JMeter" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

# Configuration
$jmeterPath = "/opt/jmeter/bin/jmeter"
$testPlan = "jmeter/performance-test.jmx"
$resultsFile = "jmeter/results.jtl"
$reportDir = "jmeter/report"

# Nettoyage des anciens rapports
Write-Host "🧹 Nettoyage des anciens rapports..." -ForegroundColor Yellow
if (Test-Path $reportDir) { Remove-Item -Recurse -Force $reportDir }
if (Test-Path $resultsFile) { Remove-Item -Force $resultsFile }
New-Item -ItemType Directory -Path $reportDir -Force | Out-Null

# Exécution du test JMeter
Write-Host "⚡ Exécution des tests de performance..." -ForegroundColor Yellow
Write-Host "   - Test Plan: $testPlan" -ForegroundColor Cyan
Write-Host "   - Résultats: $resultsFile" -ForegroundColor Cyan
Write-Host "   - Rapport: $reportDir" -ForegroundColor Cyan

try {
    & $jmeterPath -n -t $testPlan -l $resultsFile -e -o $reportDir
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n✅ Tests terminés avec succès!" -ForegroundColor Green
        
        # Affichage des statistiques
        if (Test-Path $resultsFile) {
            Write-Host "`n📊 Statistiques des résultats:" -ForegroundColor Green
            $lines = Get-Content $resultsFile | Select-Object -Last 10
            foreach ($line in $lines) {
                Write-Host "   $line" -ForegroundColor White
            }
        }
        
        # Vérification du rapport HTML
        if (Test-Path "$reportDir/index.html") {
            Write-Host "`n📈 Rapport HTML généré: $reportDir/index.html" -ForegroundColor Green
            Write-Host "   Ouvrez ce fichier dans votre navigateur pour voir les dashboards" -ForegroundColor Cyan
        }
        
        # Statistiques rapides
        Write-Host "`n📈 Métriques de performance:" -ForegroundColor Green
        if (Test-Path $resultsFile) {
            $totalRequests = (Get-Content $resultsFile | Measure-Object -Line).Lines
            $successRequests = (Get-Content $resultsFile | Select-String "200" | Measure-Object -Line).Lines
            $errorRequests = (Get-Content $resultsFile | Select-String "403|404|500" | Measure-Object -Line).Lines
            
            Write-Host "   - Total des requêtes: $totalRequests" -ForegroundColor White
            Write-Host "   - Requêtes réussies: $successRequests" -ForegroundColor Green
            Write-Host "   - Requêtes en erreur: $errorRequests" -ForegroundColor Red
            
            if ($totalRequests -gt 0) {
                $successRate = [math]::Round(($successRequests / $totalRequests) * 100, 2)
                Write-Host "   - Taux de succès: $successRate%" -ForegroundColor Cyan
            }
        }
    }
    else {
        Write-Host "`n❌ Erreur lors de l'exécution des tests" -ForegroundColor Red
    }
}
catch {
    Write-Host "`n❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Vérifiez que JMeter est installé et accessible" -ForegroundColor Yellow
}

Write-Host "`n🎯 Script terminé!" -ForegroundColor Green 