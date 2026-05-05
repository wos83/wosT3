@echo off
setlocal EnableDelayedExpansion

echo ========================================
echo GITHUB RELEASE GENERATOR
echo ========================================
echo.

:: ============================================
:: VERIFICA GH CLI
:: ============================================
gh --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] GitHub CLI not found
    echo Install at: https://cli.github.com
    pause
    exit /b 1
)

:: ============================================
:: VERIFICA GIT
:: ============================================
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Git not found
    pause
    exit /b 1
)

:: ============================================
:: VERIFICA AUTENTICACAO GH
:: ============================================
gh auth status >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Not authenticated with GitHub
    echo Run: gh auth login
    pause
    exit /b 1
)

:: ============================================
:: GERA TAG AUTOMATICA
:: ============================================
echo Generating tag...
for /f "tokens=*" %%a in ('powershell -NoProfile -Command "Get-Date -Format yyMMdd-HHmm"') do set "tagRaw=%%a"
set "tagName=v%tagRaw%"
set "releaseName=Release %tagName%"

echo.
echo ========================================
echo Tag: %tagName%
echo Release Name: %releaseName%
echo ========================================
echo.

:: ============================================
:: VERIFICA SE TAG JA EXISTE
:: ============================================
echo Checking if tag exists...

:: Verifica local
set "localExists=0"
for /f "tokens=*" %%a in ('git tag -l %tagName% 2^>nul') do set "localExists=1"

:: Verifica remote
set "remoteExists=0"
for /f "tokens=*" %%a in ('git ls-remote --tags origin %tagName% 2^>nul') do set "remoteExists=1"

if %localExists% equ 1 (
    echo [WARNING] Tag already exists locally
    echo Deleting local tag...
    git tag -d %tagName% 2>nul
)

if %remoteExists% equ 1 (
    echo [WARNING] Tag already exists on remote
    echo Deleting remote tag...
    git push origin --delete %tagName% 2>nul
)

:: ============================================
:: CRIA TAG LOCAL
:: ============================================
echo.
echo Creating tag %tagName%...
git tag %tagName%
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create tag
    pause
    exit /b 1
)
echo [OK] Tag created locally

:: ============================================
:: ENVIA TAG PARA REMOTE
:: ============================================
echo.
echo Pushing tag to GitHub...
git push origin %tagName%
if %errorlevel% neq 0 (
    echo [ERROR] Failed to push tag
    pause
    exit /b 1
)
echo [OK] Tag pushed to remote

:: ============================================
:: CRIA RELEASE NO GITHUB
:: ============================================
echo.
echo Creating release on GitHub...
gh release create %tagName% --title "%releaseName%" --notes "Release %tagName% generated automatically" --target develop
if %errorlevel% neq 0 (
    echo [ERROR] Failed to create release
    echo.
    echo [INFO] Tag was pushed successfully
    echo You can create release manually at:
    echo https://github.com/wos83/wosT3/releases/new?tag=%tagName%
    pause
    exit /b 1
)

echo [OK] Release created

:: ============================================
:: SUCESSO
:: ============================================
echo.
echo ========================================
echo [SUCCESS] RELEASE COMPLETE
echo ========================================
echo Tag Name: %tagName%
echo Release: %tagName%
echo.
echo View at: https://github.com/wos83/wosT3/releases/tag/%tagName%
echo ========================================

pause
endlocal