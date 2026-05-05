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
- Botão "LVL" para alterar a dificuldade da IA
- Suporte a Android 7.0+ (API 24+)

### Tecnologia
- Kotlin
- Jetpack Compose
- Material Design 3
- MVVM Architecture
- Minimax Algorithm para IA
- Armazenamento local para placar

## Como Jogar

1. Toque em uma célula vazia para fazer sua jogada
2. O jogador X sempre começa
3. O primeiro a formar uma linha de 3 símbolos ganha
4. Use o botão "NOVO" para iniciar uma nova partida

## Instalação

### Via APK
Instale o arquivo `app-debug.apk` diretamente no dispositivo Android.

### Via Código Fonte
```bash
# Clone o repositório
git clone https://github.com/seu-usuario/tic-tac-toe.git

# Abra no Android Studio ou build via linha de comando
./gradlew assembleDebug
```

## Screenshots

O app possui interface com:
- Título "JOGO DA VELHA" em dourado
- Placar para Player X e Player O
- Tabuleiro 3x3 com células interativas
- Botões de controle: NOVO, AI, LVL
- Modo AI com indicador de dificuldade

## Licença

MIT License