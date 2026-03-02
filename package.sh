#!/bin/bash

set -e

# Variables -----------------------------------------------------------------

APP_NAME="Shooting Stars"
INPUT_PATH="app/build/libs"
JAR_NAME="shooting-stars"

DIST_DIR="app/build"
WIN_BUILD_DIR="$DIST_DIR/win"
MAC_BUILD_DIR="$DIST_DIR/mac"
JRE_DIR="$DIST_DIR/jre"
JAR_TASK="shadowjar"

# Process -------------------------------------------------------------------

check_command() {
    local cmd="$1"
    if ! command -v "$cmd" &>/dev/null; then
        echo "[INF] ERROR: '$cmd' not found. Make sure you have a full JDK 21+ installed"
        case "$(uname -s)" in
            MINGW*|MSYS*|CYGWIN*)
                if [[ -z "$JAVA_HOME" ]]; then
                    echo "[ERR] JAVA_HOME is not set!"
                    echo "[INF] Set JAVA_HOME to your JDK installation directory"
                else
                    echo "[INF] JAVA_HOME is set to: $JAVA_HOME"
                    if [[ -x "$JAVA_HOME/bin/$cmd" ]]; then
                        echo "[ERR] It looks like the JDK exists, but it's not in your PATH"
                        echo "[INF] Consider adding: $JAVA_HOME/bin to your PATH"
                    else
                        echo "[ERR] JAVA_HOME does not appear to contain a valid JDK"
                    fi
                fi
                ;;
        esac
        exit 1
    fi
    echo "[INF] Found: $cmd ($(command -v "$cmd"))"
}

check_command java
check_command jdeps
check_command jlink
check_command jpackage

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
if [ "$JAVA_VERSION" -ge 21 ]; then
    echo "[INF] Java version is $JAVA_VERSION, which meets the requirement of 21 or higher."
else
    echo "[ERR] Java version is lower than 21. Please update Java."
    exit 1
fi

echo "[INF] Using java at $(which java)"

echo "[INF] Building JAR with Gradle..."
./gradlew $JAR_TASK

# Java modules for JRE
echo "[INF] Getting required modules..."
MODULES=$(jdeps --multi-release 21 --print-module-deps --ignore-missing-deps "$INPUT_PATH/$JAR_NAME.jar" | tr ',' ',')

# Diet JRE
echo "[INF] Creating custom JRE..."
rm -rf "$JRE_DIR"

jlink \
    --module-path "$JAVA_HOME/jmods" \
    --add-modules "$MODULES" \
    --output "$JRE_DIR" \
    --strip-debug \
    --no-header-files \
    --no-man-pages

if [[ "$OSTYPE" == "darwin"* ]]; then
    # MacOS DMG
    mkdir -p "$MAC_BUILD_DIR"
    echo "[INF] MacOS detected. Creating macOS DMG..."
    jpackage \
        --input "$INPUT_PATH" \
        --main-jar "$JAR_NAME.jar" \
        --name "$APP_NAME" \
        --type dmg \
        --dest "$MAC_BUILD_DIR" \
        --app-version 1.0 \
        --runtime-image "$JRE_DIR" \
        --icon "assets/icons/mac-icon.icns"

    echo "[INF] DMG created at $MAC_BUILD_DIR/$APP_NAME.dmg"

elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    # Windows EXE
    mkdir -p "$WIN_BUILD_DIR"
    echo "[INF] Windows detected. Creating EXE..."
    jpackage \
        --input "$INPUT_PATH" \
        --main-jar "$JAR_NAME.jar" \
        --name "$APP_NAME" \
        --type exe \
        --dest "$WIN_BUILD_DIR" \
        --app-version 1.0 \
        --runtime-image "$JRE_DIR" \
        --win-menu \
        --win-dir-chooser \
        --win-shortcut \
        --icon "assets/icons/win-icon.ico"

    echo "[INF] EXE created at $WIN_BUILD_DIR/$APP_NAME.exe"
else
    echo "[INF] Unsupported OS type: $OSTYPE"
    exit 1
fi

echo "[SUC] Packaging complete!"
