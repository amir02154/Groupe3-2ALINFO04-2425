# Script PowerShell pour exécuter le plan JMeter de démo et vérifier le rapport
$JMETER = "C:/opt/jmeter/bin/jmeter.bat"
$PLAN = "C:/Users/galaxie/IdeaProjects/Groupe3-2ALINFO4-2425/jmeter/performance-test-demo.jmx"
$RESULTS = "C:/Users/galaxie/IdeaProjects/Groupe3-2ALINFO4-2425/jmeter/results.jtl"
$REPORT = "C:/Users/galaxie/IdeaProjects/Groupe3-2ALINFO4-2425/jmeter/report"

if (Test-Path $RESULTS) { Remove-Item $RESULTS }
if (Test-Path $REPORT) { Remove-Item $REPORT -Recurse -Force }

Write-Host "\n--- Exécution du plan JMeter de démonstration ---" -ForegroundColor Cyan
& $JMETER -n -t $PLAN -l $RESULTS -e -o $REPORT

Write-Host "\n--- Résultats JMeter (20 premières lignes) ---" -ForegroundColor Yellow
if (Test-Path $RESULTS) {
    Get-Content $RESULTS -TotalCount 20
} else {
    Write-Host "Aucun fichier results.jtl généré !" -ForegroundColor Red
}

Write-Host "\n--- Fichiers du rapport ---" -ForegroundColor Yellow
if (Test-Path $REPORT) {
    Get-ChildItem $REPORT
    Write-Host "\nOuvre $REPORT\index.html dans ton navigateur pour voir les courbes et dashboards." -ForegroundColor Green
} else {
    Write-Host "Aucun dossier report généré !" -ForegroundColor Red
} 