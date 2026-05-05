@echo off
setlocal EnableDelayedExpansion

echo ========================================
echo GITHUB RELEASE GENERATOR
echo ========================================
echo.

gh --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: GitHub CLI nao encontrado
    pause
    exit /b 1
)

git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Git nao encontrado
    pause
    exit /b 1
)

echo Generating tag...
for /f "tokens=*" %%a in ('powershell -NoProfile -Command "Get-Date -Format yyMMdd-HHmm"') do set "tagRaw=%%a"
set "tagName=v%tagRaw%"
set "releaseName=Release %tagName%"

echo Tag: %tagName%
echo Release: %releaseName%
echo.

git tag -l %tagName% 2>nul
if %errorlevel% equ 0 (
    echo ERROR: Tag %tagName% already exists
    pause
    exit /b 1
)

echo Creating tag %tagName%...
git tag %tagName%
if %errorlevel% neq 0 (
    echo ERROR creating tag
    pause
    exit /b 1
)

echo Pushing tag to GitHub...
git push origin %tagName%
if %errorlevel% neq 0 (
    echo ERROR pushing tag
    pause
    exit /b 1
)

echo Creating release on GitHub...
gh release create %tagName% --title "%releaseName%" --notes "Release %tagName% generated automatically" --target develop
if %errorlevel% neq 0 (
    echo ERROR creating release
    pause
    exit /b 1
)

echo.
echo ========================================
echo RELEASE COMPLETE
echo Tag: %tagName%
echo ========================================

pause
endlocal