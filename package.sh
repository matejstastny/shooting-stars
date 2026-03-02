#!/bin/bash
# -----------------------------------------------------------------------------
# package.sh — Build a native installer using jpackage + jlink
#
# Usage:
#   ./package.sh [OPTIONS]
#
# Options:
#   --version <ver>   Override the app version           (default: APP_VERSION)
#   --skip-build      Skip the Gradle build step
#   --help            Show this message
#
# Requirements:
#   All platforms : JDK 21+
#   Windows only  : WiX 3.x  (jpackage does NOT support WiX 4+)
#                   Set JAVA_HOME and WIX as system environment variables.
# -----------------------------------------------------------------------------

set -euo pipefail

# =============================================================================
# Configuration — edit these to adapt the script to another project
# =============================================================================

APP_NAME="Shooting Stars"
APP_VERSION="1.1.0"
MAIN_JAR="shooting-stars.jar"          # filename of the fat JAR
JAVA_MIN_VERSION=21
GRADLE_BUILD_TASK="shadowjar"

INPUT_DIR="app/build/libs"             # directory that contains MAIN_JAR
OUTPUT_DIR="app/build/dist"            # installer output root
JRE_DIR="app/build/jre"               # jlink output (rebuilt every run)

MAC_ICON="assets/icons/mac-icon.icns"
WIN_ICON="assets/icons/win-icon.ico"

# Extra jpackage flags per platform (space-separated, appended verbatim)
MAC_JPACKAGE_OPTS=""
WIN_JPACKAGE_OPTS="--win-menu --win-dir-chooser --win-shortcut"

# =============================================================================
# Logging
# =============================================================================

# Use ANSI colours only when writing to a real terminal
if [[ -t 1 ]]; then
	_R="\033[0m" _BOLD="\033[1m"
	_RED="\033[31m" _GREEN="\033[32m" _YELLOW="\033[33m" _CYAN="\033[36m"
else
	_R="" _BOLD="" _RED="" _GREEN="" _YELLOW="" _CYAN=""
fi

log_inf() { echo -e "${_CYAN}[INF]${_R} $*"; }
log_ok()  { echo -e "${_GREEN}[OK ]${_R} $*"; }
log_wrn() { echo -e "${_YELLOW}[WRN]${_R} $*"; }
log_err() { echo -e "${_RED}[ERR]${_R} $*" >&2; }
die()     { log_err "$*"; exit 1; }

# =============================================================================
# Helpers
# =============================================================================

print_help() {
	grep '^#' "$0" | sed -n '2,/^[^#]/{ /^[^#]/q; s/^# \?//p }'
	exit 0
}

# Require a command to be on PATH; die with an optional hint if not found
require_cmd() {
	local cmd="$1" hint="${2:-}"
	if ! command -v "$cmd" &>/dev/null; then
		log_err "'$cmd' not found.${hint:+  $hint}"
		exit 1
	fi
	log_inf "Found: ${_BOLD}$cmd${_R} → $(command -v "$cmd")"
}

# Convert a Windows-style path to a Unix path (Git Bash / Cygwin)
to_unix_path() {
	local p="$1"
	if command -v cygpath &>/dev/null; then
		cygpath -u "$p"
	else
		p="/${p:0:1,,}${p:2}"
		echo "${p//\\//}"
	fi
}

# =============================================================================
# Argument parsing
# =============================================================================

SKIP_BUILD=false

while [[ $# -gt 0 ]]; do
	case "$1" in
		--version)    APP_VERSION="$2"; shift 2 ;;
		--skip-build) SKIP_BUILD=true;  shift   ;;
		--help | -h)  print_help ;;
		*) die "Unknown option: $1" ;;
	esac
done

# =============================================================================
# Environment setup (Windows path normalisation)
# =============================================================================

OS="$(uname -s)"

case "$OS" in
MINGW* | MSYS* | CYGWIN*)
	[[ -z "${JAVA_HOME:-}" ]] && die "JAVA_HOME is not set. Point it to your JDK $JAVA_MIN_VERSION+ installation directory."
	JAVA_HOME="$(to_unix_path "$JAVA_HOME")"
	export JAVA_HOME PATH="$JAVA_HOME/bin:$PATH"
	log_inf "JAVA_HOME → $JAVA_HOME"

	[[ -z "${WIX:-}" ]] && {
		log_err "WIX is not set. jpackage requires WiX 3 (WiX 4+ is NOT supported)."
		log_inf "1. Download WiX 3: https://github.com/wixtoolset/wix3/releases/latest"
		log_inf "2. Install it, then add a system variable:  WIX=<install directory>"
		exit 1
	}
	WIX="$(to_unix_path "$WIX")"
	export WIX PATH="$WIX/bin:$PATH"
	log_inf "WIX       → $WIX"
	;;
esac

# =============================================================================
# Prerequisite checks
# =============================================================================

log_inf "Checking prerequisites..."

require_cmd java
require_cmd jdeps    "Ensure JAVA_HOME points to a full JDK, not a JRE."
require_cmd jlink    "Ensure JAVA_HOME points to a full JDK, not a JRE."
require_cmd jpackage "Ensure JAVA_HOME points to a full JDK, not a JRE."

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F. '{print $1}')
[[ "$JAVA_VERSION" -ge "$JAVA_MIN_VERSION" ]] \
	|| die "Java $JAVA_VERSION detected — Java $JAVA_MIN_VERSION+ is required."
log_inf "Java version: ${_BOLD}$JAVA_VERSION${_R}"

case "$OS" in
MINGW* | MSYS* | CYGWIN*)
	require_cmd candle.exe "Ensure WIX points to the root WiX install directory, not the bin subfolder."
	require_cmd light.exe  "Ensure WIX points to the root WiX install directory, not the bin subfolder."
	;;
esac

# =============================================================================
# Build
# =============================================================================

if [[ "$SKIP_BUILD" == true ]]; then
	log_wrn "Skipping Gradle build (--skip-build)."
else
	log_inf "Building JAR with Gradle..."
	./gradlew "$GRADLE_BUILD_TASK" --quiet
	log_ok "JAR built → $INPUT_DIR/$MAIN_JAR"
fi

# =============================================================================
# Custom JRE (jlink)
# =============================================================================

log_inf "Resolving required modules..."
MODULES=$(jdeps --multi-release "$JAVA_MIN_VERSION" --print-module-deps \
	--ignore-missing-deps "$INPUT_DIR/$MAIN_JAR")
log_inf "Modules: $MODULES"

log_inf "Building custom JRE..."
rm -rf "$JRE_DIR"
jlink \
	--module-path   "$JAVA_HOME/jmods" \
	--add-modules   "$MODULES" \
	--output        "$JRE_DIR" \
	--strip-debug \
	--no-header-files \
	--no-man-pages
log_ok "Custom JRE → $JRE_DIR"

# =============================================================================
# Package
# =============================================================================

case "$OS" in
Darwin*)
	OUT_DIR="$OUTPUT_DIR/mac"
	mkdir -p "$OUT_DIR"
	log_inf "Packaging macOS DMG..."
	# shellcheck disable=SC2086
	jpackage \
		--input         "$INPUT_DIR" \
		--main-jar      "$MAIN_JAR" \
		--name          "$APP_NAME" \
		--app-version   "$APP_VERSION" \
		--type          dmg \
		--dest          "$OUT_DIR" \
		--runtime-image "$JRE_DIR" \
		--icon          "$MAC_ICON" \
		$MAC_JPACKAGE_OPTS
	log_ok "DMG → $OUT_DIR"
	;;

MINGW* | MSYS* | CYGWIN*)
	OUT_DIR="$OUTPUT_DIR/win"
	mkdir -p "$OUT_DIR"
	log_inf "Packaging Windows EXE..."
	# shellcheck disable=SC2086
	jpackage \
		--input         "$INPUT_DIR" \
		--main-jar      "$MAIN_JAR" \
		--name          "$APP_NAME" \
		--app-version   "$APP_VERSION" \
		--type          exe \
		--dest          "$OUT_DIR" \
		--runtime-image "$JRE_DIR" \
		--icon          "$WIN_ICON" \
		$WIN_JPACKAGE_OPTS
	log_ok "EXE → $OUT_DIR"
	;;

*)
	die "Unsupported OS: $OS"
	;;
esac

log_ok "Done. Packaged ${_BOLD}$APP_NAME $APP_VERSION${_R}."
