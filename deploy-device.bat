@echo off
REM Script para compilar e instalar no Samsung S23

echo ========================================
echo   Deploy para Samsung S23
echo ========================================
echo.

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
set "ANDROID_HOME=C:\Users\User\AppData\Local\Android\Sdk"
set "DEVICE_ID=RQCXA02MGXY"

echo [1] Compilando APK...
call gradlew.bat assembleDebug --no-daemon

if %ERRORLEVEL% neq 0 (
    echo.
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo.
echo [2] Instalando no device %DEVICE_ID%...
adb -s %DEVICE_ID% install -r app/build/outputs/apk/debug/app-debug.apk

if %ERRORLEVEL% neq 0 (
    echo.
    echo Erro na instalacao!
    pause
    exit /b 1
)

echo.
echo [3] Iniciando app...
adb -s %DEVICE_ID% shell am start -n com.tictactoe/.MainActivity

echo.
echo ========================================
echo   Deploy concluido com sucesso!
echo ========================================
echo.

pause