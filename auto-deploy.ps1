# Auto-deploy script for Tic Tac Toe Android app
# Watches for source code changes and deploys to Samsung S23

param(
    [int]$DebounceSeconds = 2,
    [string]$DeviceSerial = "RQCXA02MGXY"
)

$projectDir = "C:\Development\WOS\tic-tac-toe"
$sourceDirs = @(
    "$projectDir\app\src\main\java",
    "$projectDir\app\src\main\res"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Auto-Deploy para Samsung S23" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
$androidHome = "C:\Users\User\AppData\Local\Android\Sdk"

Write-Host "Ambiente configurado:" -ForegroundColor Yellow
Write-Host "  Java: $javaHome" -ForegroundColor Gray
Write-Host "  Android SDK: $androidHome" -ForegroundColor Gray
Write-Host "  Device: $DeviceSerial" -ForegroundColor Gray
Write-Host ""

function Build-And-Deploy {
    Write-Host "`n[$(Get-Date -Format 'HH:mm:ss')] Alteração detectada! Compilando..." -ForegroundColor Yellow
    
    $env:JAVA_HOME = $javaHome
    $env:ANDROID_HOME = $androidHome
    $env:PATH = "$javaHome\bin;$env:PATH"
    
    Set-Location $projectDir
    
    $buildResult = & .\gradlew.bat assembleDebug --no-daemon 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✓ Build concluído" -ForegroundColor Green
        
        Write-Host "  → Instalando no device..." -ForegroundColor Cyan
        $installResult = adb -s $DeviceSerial install -r app/build/outputs/apk/debug/app-debug.apk 2>&1
        
        if ($installResult -like "*Success*") {
            Write-Host "  ✓ APK instalado com sucesso!" -ForegroundColor Green
            
            Write-Host "  → Iniciando app..." -ForegroundColor Cyan
            adb -s $DeviceSerial shell am start -n com.tictactoe/.MainActivity
            Write-Host "  ✓ App iniciado!" -ForegroundColor Green
        } else {
            Write-Host "  ✗ Erro na instalação: $installResult" -ForegroundColor Red
        }
    } else {
        Write-Host "  ✗ Erro no build" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host "Aguardando novas alterações..." -ForegroundColor Cyan
}

Write-Host "Iniciando monitoramento de arquivos..." -ForegroundColor Cyan
Write-Host "Dirs monitorados:" -ForegroundColor Gray
foreach ($dir in $sourceDirs) {
    Write-Host "  - $dir" -ForegroundColor Gray
}
Write-Host ""
Write-Host "Pressione Ctrl+C para parar o monitoramento`n" -ForegroundColor Yellow

$watcher = New-Object System.IO.FileSystemWatcher
$watcher.IncludeSubdirectories = $true
$watcher.EnableRaisingEvents = $false

foreach ($dir in $sourceDirs) {
    if (Test-Path $dir) {
        $watcher.Path = $dir
        $watcher.Filter = "*.*"
        $watcher.IncludeSubdirectories = $true
        $watcher.EnableRaisingEvents = $true
    }
}

$lastBuild = Get-Date
$isBuilding = $false

$action = {
    $script:isBuilding = $true
    $script:lastBuild = Get-Date
    
    Start-Sleep -Seconds $DebounceSeconds
    
    if (-not $script:isBuilding) {
        $script:isBuilding = $false
        return
    }
    
    Build-And-Deploy
    $script:isBuilding = $false
}

Register-ObjectEvent $watcher "Changed" -Action $action
Register-ObjectEvent $watcher "Created" -Action $action
Register-ObjectEvent $watcher "Updated" -Action $action

Write-Host "Pronto! Faça uma alteração em qualquer arquivo fonte..." -ForegroundColor Green

while ($true) {
    Start-Sleep -Seconds 1
}