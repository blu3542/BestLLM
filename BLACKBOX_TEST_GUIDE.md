# Black-Box Test Running Guide

## Important: Disable Animations First!

Espresso tests require animations to be disabled. Do this **before** running tests:

### Method 1: Via ADB (Recommended)
```bash
adb shell settings put global animator_duration_scale 0
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
```

### Method 2: Via Device Settings
1. Go to Settings → Developer Options
2. Find "Window animation scale", "Transition animation scale", "Animator duration scale"
3. Set all to "Animation off" or "0x"

### Re-enable Animations After Testing
```bash
adb shell settings put global animator_duration_scale 1
adb shell settings put global window_animation_scale 1
adb shell settings put global transition_animation_scale 1
```

## Running Black-Box Tests

### In Android Studio:
1. **Disable animations first** (see above)
2. Start an emulator or connect a device
3. Right-click `CompleteBlackBoxTestSuite.java`
4. Select "Run 'CompleteBlackBoxTestSuite'"
5. Tests will execute on the device

### Via Command Line:
```bash
# Disable animations
adb shell settings put global animator_duration_scale 0
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0

# Run tests
./gradlew connectedDebugAndroidTest

# Re-enable animations
adb shell settings put global animator_duration_scale 1
adb shell settings put global window_animation_scale 1
adb shell settings put global transition_animation_scale 1
```

## Understanding Test Failures

Some tests may fail because:
1. **Missing test data**: Tests use `TEST_POST_ID` which may not exist in Firebase
2. **Activities finish early**: PostDetailActivity finishes if post doesn't exist
3. **Animations enabled**: Causes Espresso to fail

The tests are designed to verify **error handling** - they check that the app doesn't crash when given invalid data.

## Test Coverage

Black-box tests verify:
- ✅ UI elements are present
- ✅ Activities launch correctly
- ✅ Error handling works (app doesn't crash)
- ✅ User flows are accessible

For full functionality testing, you need:
- Firebase test data (real posts, comments)
- Or Firebase emulator with test data setup

