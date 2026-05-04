#!/bin/bash

# Script para fazer commit e push para a branch develop

echo "Iniciando deploy para branch develop..."

# Inicializa git se necessário
if [ ! -d .git ]; then
    echo "Inicializando repositório Git..."
    git init
fi

# Adiciona todos os arquivos
echo "Adicionando arquivos..."
git add .

# Pede a mensagem de commit
echo -n "Digite a mensagem do commit: "
read COMMIT_MSG

if [ -z "$COMMIT_MSG" ]; then
    COMMIT_MSG="Initial commit: Tic Tac Toe Android game with Jetpack Compose"
fi

# Faz o commit
echo "Criando commit..."
git commit -m "$COMMIT_MSG"

# Cria ou altera para branch develop
echo "Alternando para branch develop..."
git branch -M develop 2>/dev/null || git checkout -b develop

# Push para origin develop
echo "Enviando para origin develop..."
git push -u origin develop

echo "Deploy concluído com sucesso!"
echo "Branch: develop"
echo "Remote: origin"