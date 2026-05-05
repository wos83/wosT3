@echo off
cd /d "%~dp0"
setlocal EnableDelayedExpansion
chcp 1252 >nul

:: ============================================
:: CONFIGURAÇÕES
:: ============================================
set "appName=Tic Tac Toe (WOS)"
set "repoUrl=https://github.com/wos83/wosT3.git"
set "branch=develop"
set "gitName=Willian Santos"
set "gitEmail=m16@m16.com.br"

echo ========================================
echo %appName% SOURCE UPDATE
echo ========================================
echo.

:: ============================================
:: VERIFICA GIT
:: ============================================

git --version >nul 2>&1
if errorlevel 1 (
    echo ERRO: Git nao encontrado
    pause
    rem exit /b
)

:: ============================================
:: DATA / HORA
:: ============================================

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyy.MM.dd"') do set dateNow=%%i
for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format HH:mm:ss"') do set timeNow=%%i

set "commitTitle=%appName% Update Source %dateNow% %timeNow%"

REM ============================================
REM CAPTURA DE HARDWARE/SISTEMA (POWERSHELL + FALLBACKS)
REM ============================================

echo Detectando informacoes do sistema...

:: -----------------------------------------------------------------
:: Melhoria: Captura robusta do MAC Address e IP Address
:: -----------------------------------------------------------------
set "psCommandMAC=$adapter=Get-NetAdapter -Physical | Where-Object {$_.Status -eq 'Up'} | Sort-Object InterfaceIndex | Select-Object -First 1; if($adapter){$adapter.MacAddress}else{''}"
for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "& {%psCommandMAC%}" 2^>nul`) do set "macAddress=%%i"

:: Se falhou, tenta via getmac (fallback)
if not defined macAddress set "macAddress="
if "!macAddress!"=="" (
    for /f "tokens=3" %%a in ('getmac /NH /FO CSV 2^>nul ^| findstr /V /C:"-"') do (
        set "macAddress=%%~a"
        goto :mac_ok
    )
)
:mac_ok

:: Captura IP: Prioriza interface com gateway padrão (conectada à internet)
set "psCommandIP=$route=Get-NetRoute -DestinationPrefix 0.0.0.0/0 -ErrorAction SilentlyContinue | Select-Object -First 1; if($route){$ip=($route | Get-NetIPAddress -AddressFamily IPv4 -ErrorAction SilentlyContinue).IPAddress; if($ip){$ip}} else {$ip=Get-NetIPAddress -AddressFamily IPv4 | Where-Object {$_.IPAddress -notlike '169.254.*'} | Sort-Object InterfaceIndex | Select-Object -First 1 -ExpandProperty IPAddress; $ip}"
for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "& {%psCommandIP%}" 2^>nul`) do set "ipAddress=%%i"

:: Fallback: usa o primeiro IP não link-local listado pelo ipconfig
if not defined ipAddress set "ipAddress="
if "!ipAddress!"=="" (
    for /f "tokens=2 delims=:" %%a in ('ipconfig 2^>nul ^| findstr /R /C:"IPv4.*: [0-9]"') do (
        for /f "tokens=* delims= " %%b in ("%%a") do (
            set "ipAddress=%%b"
            goto :ip_ok
        )
    )
)
:ip_ok

:: -----------------------------------------------------------------
:: Melhoria: Captura do nome da CPU com múltiplas tentativas
:: -----------------------------------------------------------------
set "cpuName="

:: 1ª tentativa: PowerShell (Get-CimInstance Win32_Processor).Name
for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "(Get-CimInstance Win32_Processor -ErrorAction SilentlyContinue | Select-Object -First 1).Name.Trim()" 2^>nul`) do set "cpuName=%%i"

:: 2ª tentativa: WMIC (caso PowerShell falhe)
if not defined cpuName (
    for /f "skip=1 tokens=* delims=" %%i in ('wmic cpu get name /value 2^>nul ^| find "="') do (
        set "wmic_line=%%i"
        set "cpuName=!wmic_line:Name=!"
        goto :cpu_ok
    )
)
:cpu_ok

:: 3ª tentativa: Variável de ambiente PROCESSOR_IDENTIFIER (fallback final)
if not defined cpuName (
    if defined PROCESSOR_IDENTIFIER (
        set "cpuName=%PROCESSOR_IDENTIFIER%"
    ) else (
        set "cpuName=Nao identificado"
    )
)

:: Remove possíveis espaços extras
if defined cpuName (
    for /f "tokens=* delims= " %%a in ("!cpuName!") do set "cpuName=%%a"
)
:: -----------------------------------------------------------------
:: Demais informações (Windows, RAM) - sem alterações
:: -----------------------------------------------------------------
for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "(Get-CimInstance Win32_OperatingSystem).Caption" 2^>nul`) do set "winVersion=%%i"

for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "(Get-CimInstance Win32_Processor ^| Select-Object -First 1).Name.Trim()" 2^>nul`) do set "cpuName=%%i"

for /f "usebackq delims=" %%i in (`powershell -NoProfile -Command "[math]::Round((Get-CimInstance Win32_ComputerSystem).TotalPhysicalMemory /1GB,2)" 2^>nul`) do set "ramTotal=%%i"

REM ===== FALLBACKS =====

if not defined macAddress set "macAddress=Nao identificado"
if not defined ipAddress set "ipAddress=Nao identificado"
if not defined winVersion set "winVersion=Nao identificado"
if not defined cpuName set "cpuName=Nao identificado"
if not defined ramTotal set "ramTotal=Nao identificado"

:: ============================================
:: LIMPEZA BUILD
:: ============================================

echo Limpando temporarios de build...

:: Limpar diretórios de build
for /d %%d in (build app\build .gradle tmp\v1a\app\build tmp\v1a\.gradle) do (
    if exist "%%d" rmdir /s /q "%%d"
)

:: Limpar arquivos de build
for %%e in (apk aab) do (
    del /s /q *.%%e 2>nul
)

:: ============================================
:: LIMPEZA IDE
:: ============================================

echo Limpando temporarios IDE...

for /d %%d in (.idea out) do (
    if exist "%%d" rmdir /s /q "%%d"
)

del /s /q *.iml 2>nul

:: ============================================
:: GIT CONFIG
:: ============================================

git config user.name "%gitName%"
git config user.email "%gitEmail%"

:: ============================================
:: BRANCH
:: ============================================

git rev-parse --verify %branch% >nul 2>&1

if errorlevel 1 (
    echo Criando branch %branch%
    git checkout -b %branch%
) else (
    git checkout %branch%
)

:: ============================================
:: ADD FILES
:: ============================================

echo.
echo Detectando alteracoes...

git add -A

git diff --cached --quiet
if %errorlevel%==0 (
    echo Nenhuma alteracao detectada
    goto end
)

:: ============================================
:: ARQUIVOS ALTERADOS
:: ============================================

git diff --cached --name-status > "%TEMP%\files.txt"
git diff --cached --shortstat > "%TEMP%\stats.txt"

(
echo !commitTitle!
echo.
echo Application: !appName!
echo Machine: !COMPUTERNAME!
echo User: !USERNAME!
echo Branch: !branch!
echo.
echo [Network]
echo MAC Address: !macAddress!
echo IP Address: !ipAddress!
echo.
echo [System]
echo Windows: !winVersion!
echo CPU: !cpuName!
echo RAM: !ramTotal! GB
echo.
echo Files:
type "%TEMP%\files.txt"
echo.
echo Stats:
type "%TEMP%\stats.txt"
) > "%TEMP%\commit.txt"

:: ============================================
:: COMMIT
:: ============================================

git commit -F "%TEMP%\commit.txt"

if errorlevel 1 (
    echo ERRO ao criar commit
    pause
    rem exit /b
)

echo Commit criado

:: ============================================
:: PULL
:: ============================================

echo Sincronizando com remoto...
git pull origin %branch% --rebase

:: ============================================
:: PUSH
:: ============================================

echo Enviando para GitHub...

git push -u origin %branch%

if errorlevel 1 (
    echo ERRO no push
    pause
    rem exit /b
)

echo.
echo ========================================
echo PUSH CONCLUIDO
echo ========================================

:end

del "%TEMP%\commit.txt" 2>nul
del "%TEMP%\files.txt" 2>nul
del "%TEMP%\stats.txt" 2>nul

pause
endlocal