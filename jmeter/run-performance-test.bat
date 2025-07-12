@echo off
echo ========================================
echo Test de Performance JMeter
echo ========================================

REM Vérifier si JMeter est installé
where jmeter >nul 2>nul
if %errorlevel% neq 0 (
    echo ERREUR: JMeter n'est pas installé ou n'est pas dans le PATH
    echo Veuillez installer JMeter et l'ajouter au PATH
    pause
    exit /b 1
)

REM Créer le dossier de sortie s'il n'existe pas
if not exist "reports" mkdir reports

REM Nettoyer les anciens rapports
if exist "reports\performance-report" rmdir /s /q "reports\performance-report"

echo.
echo Démarrage du test de performance...
echo.

REM Exécuter JMeter en mode non-GUI avec génération du rapport HTML
jmeter -n -t performance-test.jmx -l results.jtl -e -o reports\performance-report

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo Test terminé avec succès!
    echo ========================================
    echo Rapport HTML généré dans: reports\performance-report
    echo Fichier de résultats: results.jtl
    echo.
    echo Ouverture du rapport dans le navigateur...
    start reports\performance-report\index.html
) else (
    echo.
    echo ========================================
    echo ERREUR lors de l'exécution du test
    echo ========================================
    echo Code d'erreur: %errorlevel%
)

echo.
pause 