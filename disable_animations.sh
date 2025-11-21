#!/bin/bash
# Script to disable animations for Espresso testing

echo "Disabling animations for Espresso testing..."
adb shell settings put global animator_duration_scale 0
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
echo "Animations disabled!"
echo ""
echo "To re-enable animations, run: enable_animations.sh"
