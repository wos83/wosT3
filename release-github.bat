@echo off
setlocal EnableDelayedExpansion
chcp 1252 >nul

:: ============================================
:: CONFIGURAÇÕES
:: ============================================
set "repoOwner=wos83"
set "repoName=wosT3"
set "branch=develop"

echo ========================================
echo GITHUB RELEASE GENERATOR
echo ========================================
echo.

:: ============================================
:: VERIFICA GH CLI
:: ============================================

gh --version >nul 2>&1
if errorlevel 1 (
    echo ERRO: GitHub CLI (gh) nao encontrado
    echo Instale em: https://cli.github.com
    pause
    exit /b 1
)

:: ============================================
:: VERIFICA GIT
:: ============================================

git --version >nul 2>&1
if errorlevel 1 (
    echo ERRO: Git nao encontrado
    pause
    exit /b 1
)

:: ============================================
:: GERA TAG BASEADO EM DATA/HORA
:: ============================================

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyMMdd"') do set dateTag=%%i
for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format HHmm"') do set timeTag=%%i

set "tagName=v%dateTag%-%timeTag%"
set "releaseName=Release %tagName%"

echo Tag: %tagName%
echo Release: %releaseName%
echo.

:: ============================================
:: VERIFICA SE TAG JA EXISTE
:: ============================================

git tag -l %tagName% >nul 2>&1
if not errorlevel 1 (
    echo ERRO: Tag %tagName% ja existe
    pause
    exit /b 1
)

:: ============================================
:: CRIA TAG
:: ============================================

echo Criando tag %tagName%...
git tag %tagName%

if errorlevel 1 (
    echo ERRO ao criar tag
    pause
    exit /b 1
)

:: ============================================
:: PUSH TAG
:: ============================================

echo Enviando tag para GitHub...
git push origin %tagName%

if errorlevel 1 (
    echo ERRO ao fazer push da tag
    pause
    exit /b 1
)

:: ============================================
:: CRIA RELEASE VIA GH CLI
:: ============================================

echo Criando release no GitHub...
gh release create %tagName% --title "%releaseName%" --notes "Release %tagName% gerada automaticamente" --target %branch%

if errorlevel 1 (
    echo ERRO ao criar release
    echo Nota: A tag foi enviada com sucesso, mas o release pode ser criado manualmente
    pause
    exit /b 1
)

echo.
echo ========================================
echo RELEASE CONCLUIDO
echo Tag: %tagName%
echo Release: %releaseName%
echo ========================================

pause
endlocal