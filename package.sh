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
		MINGW* | MSYS* | CYGWIN*)
			echo "[INF] Detected Windows environment."

			if [[ -z "$JAVA_HOME" ]]; then
				echo "[ERR] JAVA_HOME is not set."
				echo "[INF] Set JAVA_HOME to your JDK installation directory."
			else
				echo "[INF] JAVA_HOME is set to: $JAVA_HOME"
				if command -v cygpath &>/dev/null; then
					unix_java_home="$(cygpath -u "$JAVA_HOME")"
				else
					unix_java_home="/${JAVA_HOME:0:1,,}${JAVA_HOME:2}"
				unix_java_home="${unix_java_home//\\//}"
				fi

				if [[ -x "$unix_java_home/bin/$cmd" ]]; then
					echo "[ERR] It looks like the JDK exists, but it's not in your PATH."
					echo "[INF] Consider adding: $unix_java_home/bin to your PATH"
				else
					echo "[ERR] JAVA_HOME does not appear to contain a valid JDK."
				fi
			fi
			;;
		esac
		exit 1
	fi
	echo "[INF] Found: $cmd ($(command -v "$cmd"))"
}

# On Windows, normalize JAVA_HOME and prepend JDK bin to PATH early
# so jdeps/jlink/jpackage are found even if only java.exe is on the system PATH
case "$(uname -s)" in
MINGW* | MSYS* | CYGWIN*)
	if [[ -n "$JAVA_HOME" ]]; then
		if command -v cygpath &>/dev/null; then
			JAVA_HOME="$(cygpath -u "$JAVA_HOME")"
		else
			JAVA_HOME="/${JAVA_HOME:0:1,,}${JAVA_HOME:2}"
			JAVA_HOME="${JAVA_HOME//\\//}"
		fi
		export PATH="$JAVA_HOME/bin:$PATH"
		echo "[INF] Normalized JAVA_HOME: $JAVA_HOME"
	fi
	;;
esac

check_command java
check_command jdeps
check_command jlink
check_command jpackage

if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
	if ! command -v candle.exe &>/dev/null || ! command -v light.exe &>/dev/null; then
		echo "[ERR] WiX tools (candle.exe, light.exe) not found."
		echo "[INF] jpackage requires WiX 3 to build EXE installers. WiX 4+ is NOT supported."
		echo "[INF] Download WiX 3 from: https://github.com/wixtoolset/wix3/releases/latest"
		echo "[INF] After installing, add the WiX bin folder to your system PATH."
		exit 1
	fi
	echo "[INF] Found WiX: $(command -v candle.exe)"
fi

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
MODULES=$(jdeps --multi-release 21 --print-module-deps --ignore-missing-deps "$INPUT_PATH/$JAR_NAME.jar")

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
