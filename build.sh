
#!/bin/bash

set -e

APP_NAME="SaleTaxRate"
VERSION="1.0.0"
MAIN_JAR="TaxRateLookUp.jar"
MAIN_CLASS="Main"
DEPENDENCY="json-simple-1.1.1.jar"

mkdir -p dist

OS="$(uname -s)"

case "$OS" in
    Darwin*)
        echo "Building macOS DMG..."

        jpackage \
            --type dmg \
            --name "$APP_NAME" \
            --app-version "$VERSION" \
            --input target \
            --main-jar "$MAIN_JAR" \
            --main-class "$MAIN_CLASS" \
            --icon icons/app-icon.icns \
            --dest dist
        ;;

    MINGW*|MSYS*|CYGWIN*)
        echo "Building Windows EXE..."

        jpackage \
            --type exe \
            --name "$APP_NAME" \
            --app-version "$VERSION" \
            --input target \
            --main-jar "$MAIN_JAR" \
            --icon icons/app-icon.ico \
            --win-dir-chooser \
            --win-menu \
            --win-menu-group "SaleTaxRate" \
            --win-shortcut \
            --win-shortcut-prompt \
            --win-per-user-install \
            --dest dist
        ;;

    Linux*)
        echo "Building Linux package..."

        jpackage \
            --type deb \
            --name "$APP_NAME" \
            --app-version "$VERSION" \
            --input target \
            --main-jar "$MAIN_JAR" \
            --main-class "$MAIN_CLASS" \
            --icon icons/app-icon.png \
            --dest dist
        ;;

    *)
        echo "Unsupported operating system: $OS"
        exit 1
        ;;
esac

echo "Build completed! Check the dist folder."
