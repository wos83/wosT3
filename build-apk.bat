@echo off
setlocal EnableDelayedExpansion

echo ========================================
echo ANDROID APK BUILDER
echo ========================================
echo.

:: ============================================
:: CONFIGURAÇÕES
:: ============================================
set "deployFolder=%~dp0deploy"
set "prefix=wost3"

:: ============================================
:: GERA NOME DO ARQUIVO
:: ============================================
echo Generating filename...
for /f "tokens=*" %%a in ('powershell -NoProfile -Command "Get-Date -Format yyMMdd-HHmm"') do set "dateStr=%%a"
set "apkName=%prefix%-%dateStr%.apk"
set "apkPath=%deployFolder%\%apkName%"

echo APK Name: %apkName%
echo.

:: ============================================
:: CRIA PASTA DEPLOY
:: ============================================
if not exist "%deployFolder%" (
    echo Creating deploy folder...
    mkdir "%deployFolder%"
)

:: ============================================
:: LIMPA BUILD ANTERIOR
:: ============================================
echo Cleaning previous build...
if exist "app\build\outputs\apk\release" rmdir /s /q "app\build\outputs\apk\release"

:: ============================================
:: BUILD APK RELEASE
:: ============================================
echo.
echo Building debug APK...
call gradlew.bat assembleDebug --no-daemon
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Build failed
    pause
    exit /b 1
)

:: ============================================
:: LOCALIZA APK GERADO
:: ============================================
set "sourceApk=app\build\outputs\apk\debug\app-debug.apk"

if not exist "%sourceApk%" (
    echo [ERROR] APK not found at %sourceApk%
    echo Searching for APK...
    dir /s /b app\build\outputs\apk\*.apk 2>nul
    pause
    exit /b 1
)

:: ============================================
:: COPIA PARA DEPLOY
:: ============================================
echo.
echo Copying to deploy folder...
copy /y "%sourceApk%" "%apkPath%" >nul
if %errorlevel% neq 0 (
    echo [ERROR] Failed to copy APK
    pause
    exit /b 1
)

:: ============================================
:: SUCESSO
:: ============================================
echo.
echo ========================================
echo [SUCCESS] APK BUILT
echo ========================================
echo File: %apkName%
echo Path: %apkPath%
echo Size: 
dir "%apkPath%" | findstr ".apk"
echo ========================================

pause
endlocal