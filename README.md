<div align="center">
  <img src="assets/banners/banner.png" alt="Shooting Stars Banner" width="100%">
</div>

<br>

**Shooting Stars** is a Java desktop game where you click on stars as fast as you can before the clock runs out. Each hit scores points, each miss costs you - beat your high score and climb your personal leaderboard.

## Features

- 30-second click reaction rounds
- Score tracking with a persistent high score
- 4 language options: **English**, **Czech**, **Japanese**, **Korean**
- Native installer support (macOS DMG / Windows EXE)

## Running from source

```bash
# Clone the repo
git clone https://github.com/matejstastny/shooting-stars.git
cd shooting-stars

# Run
./gradlew run
```

## Building a native installer

The `package.sh` script builds a platform-specific installer using `jpackage` and `jlink`:

```bash
./package.sh
```

- **macOS** → `app/build/mac/Shooting Stars.dmg`
- **Windows** → `app/build/win/Shooting Stars.exe`

## Settings & data

User data is stored locally and persists between sessions:

| Platform      | Path                                 |
| ------------- | ------------------------------------ |
| macOS / Linux | `~/.ShootingStars/user_data/`        |
| Windows       | `%APPDATA%\ShootingStars\user_data\` |

To reset your score and settings, use the **Delete Data** button in the in-game settings panel.

## Tech stack

|          |                                   |
| -------- | --------------------------------- |
| Language | Java 21                           |
| UI       | Java Swing                        |
| Build    | Gradle + Shadow JAR               |
| Data     | Jackson (JSON), Apache POI (XLSX) |
| Logging  | Log4j2                            |
