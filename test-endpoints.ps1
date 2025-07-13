# Script de test des endpoints pour vérifier les données
Write-Host "🧪 Test des endpoints de l'application Foyer" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Green

$baseUrl = "http://localhost:8086/Foyer"
$endpoints = @(
    "universite/findAll",
    "foyer/findAll", 
    "bloc/findAll",
    "chambre/findAll",
    "etudiant/findAll",
    "reservation/findAll"
)

foreach ($endpoint in $endpoints) {
    $url = "$baseUrl/$endpoint"
    Write-Host "`n📡 Test de: $endpoint" -ForegroundColor Yellow
    
    try {
        $response = Invoke-RestMethod -Uri $url -Method GET -ContentType "application/json" -TimeoutSec 10
        $count = if ($response -is [array]) { $response.Count } else { 1 }
        Write-Host "✅ Succès: $count élément(s) trouvé(s)" -ForegroundColor Green
        
        if ($count -eq 0) {
            Write-Host "⚠️  Attention: Aucune donnée trouvée" -ForegroundColor Yellow
        }
    }
    catch {
        Write-Host "❌ Erreur: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n🎯 Test terminé!" -ForegroundColor Green 