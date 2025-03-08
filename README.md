![banner](https://github.com/kireiiiiiiii/shooting-stars/blob/master/assets/.github/banner.png?raw=true)

# ⭐ Shooting Stars ⭐

This is a game built using **Java 21** and **Gradle 8.13**.

## 📥 Installation

1. Download the universal `.jar` file from the [latest release](https://github.com/kireiiiiiiii/shooting-stars/releases/latest).
2. Ensure you have **Java 21** or higher installed on your system.
3. Run the `.jar` file by double clicking it or from the terminal.

## 🎯 Goal of the Game

Shoot as many stars as possible before time runs out! ⏳✨

- Each successfully shot star **earns you +10 points**.
- Clicking outside the target **subtracts 10 points**, but your score can’t go negative.
- Aim for the **highest score** before the timer hits zero!

## 🎮 Controls

- **S Key** ➝ Start the game (Main Menu)
- **Left Mouse Click** ➝ Shoot the star target
- **R Key** ➝ Restart the game
- **ESC Key** ➝ Pause the game

## 🛠️ Running from Source

To run the game from the source code, you need to clone this repository:

```shell
git clone https://github.com/kireiiiiiiii/shooting-stars.git
cd shooting-stars
```

And then run the Gradle `run` task, which will use [ShadowJar](https://github.com/GradleUp/shadow) to package and execute the game:

On macOS/Linux:

```shell
./gradlew run
```

On Windows:

```shell
gradlew.bat run
```

## 📦 Packaging

This project includes shell scripts to package the game into a **`.dmg` (macOS)** or **`.exe` (Windows)** using [jpackage](https://docs.oracle.com/en/java/javase/17/docs/specs/man/jpackage.html). These scripts include a custom JRE and automatically detect the OS. How to package the game:

1. Clone this repository:

```shell
git clone https://github.com/kireiiiiiiii/shooting-stars.git
cd shooting-stars
```

2. Run the packaging script:

```shell
sh package.sh
```

- On **macOS**, this will generate a `.dmg` file.
- On **Windows**, this will generate an `.exe` file.
- **Other platforms are not supported** due to OS-specific dependencies.

## 🚀 Future Goals / Tasks

- ✅ Improve target placement (no overlapping with the scoreboard or edges).
- ✅ Add **random target rotation** for variety.
- ✅ Enhance hitbox detection (include star tips, not just the center).
- ✅ Add **sound effects and music** for a better experience.
- ✅ Include an **exit button** in the main menu.

## 📩 Contact

💬 Have feedback, issues, or suggestions? Open an **issue** or contact me on Instagram: [@\_kireiiiiiiii](https://www.instagram.com/_kireiiiiiiii)
