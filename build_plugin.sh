#!/bin/bash
# Build Plugin Helper Script
# Usage: ./build_plugin.sh ClassName [url]
# Example: ./build_plugin.sh GreenButton
# Example: ./build_plugin.sh GreenButton https://example.com

if [ $# -eq 0 ]; then
    echo "Build Plugin Helper"
    echo "=================="
    echo ""
    echo "Usage: ./build_plugin.sh ClassName [url]"
    echo ""
    echo "Arguments:"
    echo "  ClassName    - Name of your button class (without .java)"
    echo "  url          - Optional: URL to open (default: https://example.com)"
    echo ""
    echo "Example:"
    echo "  ./build_plugin.sh MyButton"
    echo "  ./build_plugin.sh GreenButton https://google.com"
    echo ""
    echo "This script will:"
    echo "  1. Compile your button class"
    echo "  2. Create a JAR file in the plugins/ folder"
    echo "  3. Output the JAR location"
    echo ""
    exit 1
fi

CLASS_NAME=$1
URL=${2:-https://example.com}
JAR_NAME="buttonstuff/plugins/${CLASS_NAME}.jar"
SRC_FILE="buttonstuff/plugins/${CLASS_NAME}.java"

# Check if source file exists
if [ ! -f "$SRC_FILE" ]; then
    echo "Error: Source file not found: $SRC_FILE"
    exit 1
fi

echo "Building plugin: $CLASS_NAME"
echo "================================"

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile the class
echo "Compiling: $SRC_FILE"
javac -cp bin "$SRC_FILE" -d bin
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

# Create JAR
echo "Creating JAR: $JAR_NAME"
jar cf "$JAR_NAME" -C bin buttonstuff/plugins/${CLASS_NAME}.class

if [ -f "$JAR_NAME" ]; then
    echo ""
    echo "✓ Successfully created: $JAR_NAME"
    echo ""
    echo "Next step: Run URLButtonDemo to load your plugin!"
    echo ""
else
    echo "✗ Failed to create JAR file"
    exit 1
fi
