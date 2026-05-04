@echo off
REM Script para fazer commit e push para a branch develop (Windows)

echo.
echo ========================================
echo   Deploy para branch develop
echo ========================================
echo.

REM Inicializa git se necessario
if not exist ".git" (
    echo [1] Inicializando repositório Git...
    git init
    git remote add origin https://github.com/wos83/wosT3.git
)

REM Verifica se remote existe, se não adiciona
git remote -v | findstr "origin" >nul
if errorlevel 1 (
    git remote add origin https://github.com/wos83/wosT3.git
)

REM Adiciona todos os arquivos
echo [2] Adicionando arquivos...
git add .

REM Pede a mensagem de commit
echo.
set /p COMMIT_MSG=Digite a mensagem do commit (Enter para padrao):
if "%COMMIT_MSG%"=="" set COMMIT_MSG=Initial commit: Tic Tac Toe Android game with Jetpack Compose

REM Faz o commit
echo [3] Criando commit...
git commit -m "%COMMIT_MSG%"

REM Cria ou altera para branch develop
echo [4] Alternando para branch develop...
git branch -M develop

REM Push para origin develop
echo [5] Enviando para origin develop...
git push -u origin develop

echo.
echo ========================================
echo   Deploy concluido com sucesso!
echo   Branch: develop
echo   Remote: https://github.com/wos83/wosT3.git
echo ========================================
echo.

pause