#!/bin/bash
# Script to run black-box (instrumented) tests

echo "Make sure you have a device/emulator running!"
echo "Checking for connected devices..."

# Check if device is connected
adb devices

echo ""
echo "Running all instrumented tests..."
./gradlew connectedDebugAndroidTest

echo ""
echo "To run with coverage:"
echo "./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.bestllm.CompleteBlackBoxTestSuite"
