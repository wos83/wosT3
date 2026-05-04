# Tic Tac Toe - Android Game

A modern, high-performance Tic Tac Toe game for Android built with Jetpack Compose and Material Design 3.

## Features

- **MVVM Architecture** - Clean separation with ViewModel and StateFlow
- **Async Processing** - All game logic runs off the main thread using Kotlin Coroutines
- **Visual Effects** - Hyper-realistic UI with dynamic shadows, gradients, and glassmorphism
- **Material You** - Dynamic color support (Android 12+)
- **Animations** - Smooth entry and marking animations with Haptic Feedback
- **Scoreboard** - Persistent score tracking for two local players
- **Edge-to-Edge** - Modern full-screen experience

## Requirements

- Android SDK 26 (Android 8.0) or higher
- Android Studio Arctic Fox or newer
- Kotlin 2.0.21
- Gradle 8.7+
- AGP 8.5.0+

## Project Structure

```
app/src/main/java/com/tictactoe/
├── MainActivity.kt          # Entry point with Edge-to-Edge support
├── viewmodel/
│   └── TicTacToeViewModel.kt # Game logic with async processing
└── ui/
    ├── theme/
    │   ├── Theme.kt         # Material 3 theming
    │   └── Typography.kt    # Typography definitions
    └── components/
        ├── Board.kt         # Game board component
        ├── GameCell.kt      # Individual cell with animations
        └── Scoreboard.kt    # Score display component
```

## Build

```bash
# Clone the repository
git clone <repository-url>

# Open in Android Studio or build from command line
./gradlew assembleDebug
```

## Output

Debug APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

## Technology Stack

- **Jetpack Compose** - Modern declarative UI
- **Material Design 3** - Latest Material components
- **Kotlin Coroutines** - Asynchronous programming
- **StateFlow** - Reactive state management
- **ViewModel** - Lifecycle-aware UI state

## License

MIT License