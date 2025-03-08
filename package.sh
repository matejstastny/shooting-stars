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

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
if [ "$JAVA_VERSION" -ge 21 ]; then
    echo "Java version is $JAVA_VERSION, which meets the requirement of 21 or higher."
else
    echo "Java version is lower than 21. Please update Java."
    exit 1
fi

echo "Building JAR with Gradle..."
./gradlew $JAR_TASK

# Diet JRE
echo "Creating custom JRE..."
rm -rf "$JRE_DIR"

MODULES=$(jdeps --print-module-deps --ignore-missing-deps "$INPUT_PATH/$JAR_NAME.jar" | tr ',' ',')

echo "Running jlink..."
jlink \
    --module-path "$(jdeps --print-module-deps --ignore-missing-deps "$INPUT_PATH/$JAR_NAME.jar" | tr ',' ';')" \
    --add-modules "$MODULES" \
    --output "$JRE_DIR" \
    --strip-debug \
    --no-header-files \
    --no-man-pages

if [[ "$OSTYPE" == "darwin"* ]]; then
    # MacOS DMG
    mkdir -p "$MAC_BUILD_DIR"
    echo "MacOS detected. Creating macOS DMG..."
    jpackage \
        --input "$INPUT_PATH" \
        --main-jar "$JAR_NAME.jar" \
        --name "$APP_NAME" \
        --type dmg \
        --dest "$MAC_BUILD_DIR" \
        --app-version 1.0 \
        --runtime-image "$JRE_DIR" \
        --icon "assets/icons/mac-icon.icns"

    echo "DMG created at $MAC_BUILD_DIR/$APP_NAME.dmg"

elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    # Windows EXE
    mkdir -p "$WIN_BUILD_DIR"
    echo "Windows detected. Creating EXE..."
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

    echo "EXE created at $WIN_BUILD_DIR/$APP_NAME.exe"
else
    echo "Unsupported OS type: $OSTYPE"
    exit 1
fi

echo "Packaging complete!"
