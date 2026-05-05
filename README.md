# Jogo da Velha - Tic Tac Toe

Um aplicativo Android de Jogo da Velha com modo PvP (player vs player) e modo PvE (player vs IA), desenvolvido em Kotlin com Jetpack Compose.

## Recursos

### Mode de Jogo
- **PvP (Player vs Player)**: Dois jogadores podem jogar no mesmo dispositivo
- **PvE (Player vs IA)**: Jogue contra a inteligência artificial
- **3 Níveis de Dificuldade**: Easy, Medium e Hard

### Interface
- **Design Retrô/Neon**: Visual moderno com elementos de霓虹灯
- **Fonte Pixelify Sans**: Fonte pixelada para completar o estilo retrô
- **Feedback Visual**: Destaque do jogador atual e linha vencedora
- **Feedback Tátil**: Vibração ao tocar nas células
- **Feedback Sonoro**: Sons para movimentos, vitória e empate

### Funcionalidades
- Placar persistente entre partidas
- Indicador de turno atual
- Detecção automática de vitória e empate
- Botão "NOVO" para reiniciar a partida
- Botão "AI" para alternar entre modo PvP e PvE
- Botão "LEVEL" para alterar a dificuldade da IA
- Suporte a Android 7.0+ (API 24+)

### Tecnologia
- Kotlin
- Jetpack Compose
- Material Design 3
- MVVM Architecture
- Minimax Algorithm para IA
- Armazenamento local para placar
- Gradle 9.4.1
- Android SDK 36
- Kotlin 2.0.21

## Como Jogar

1. Toque em uma célula vazia para fazer sua jogada
2. O jogador X sempre começa
3. O primeiro a formar uma linha de 3 símbolos ganha
4. Use o botão "NOVO" para iniciar uma nova partida

## Instalação

### Via APK (Pasta deploy)
O arquivo APK já compilado está disponível na pasta `deploy/`:
```
deploy/wost3-YYMMDD-HHMM.apk
```

### Via Código Fonte
```bash
# Clone o repositório
git clone https://github.com/wos83/wosT3.git

# Build via linha de comando
gradlew.bat assembleDebug
```

## Scripts de Deployment

O projeto possui 3 scripts batch para automatizar o build e deployment:

### 1. build-apk.bat
Gera o arquivo APK de instalação e salva na pasta `deploy/`.

**Uso:**
```batch
build-apk.bat
```

**Saída:**
- Arquivo: `deploy/wost3-YYMMDD-HHMM.apk`

### 2. deploy-github.bat
Faz o deploy do código fonte para o repositório GitHub.

**Uso:**
```batch
deploy-github.bat
```

**Funcionalidades:**
- Limpa arquivos temporários de build (.gradle, .idea, build)
- Limpa arquivos temporários de IDE
- Faz commit com informações do sistema (machine, user, IP, MAC, CPU, RAM)
- Pull com rebase
- Push para branch develop

### 3. release-github.bat
Cria uma tag e release no GitHub.

**Uso:**
```batch
release-github.bat
```

**Saída:**
- Tag: `vYYMMDD-HHMM` (ex: v260505-1628)
- Release no GitHub com nome "Release vYYMMDD-HHMM"

## Screenshots

O app possui interface com:
- Título "JOGO DA VELHA" em dourado
- Placar para Player X e Player O
- Tabuleiro 3x3 com células interativas
- Botões de controle: NOVO, AI, LEVEL
- Modo AI com indicador de dificuldade

## Requisitos de Build

- Java 25 (JDK 25)
- Gradle 9.4.1 (incluído no wrapper)
- Android SDK 36
- Kotlin 2.0.21
- Windows (para executar os scripts .bat)

## Licença

MIT License