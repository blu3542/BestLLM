# Code Coverage Testing Instructions

## Quick Start - Android Studio

1. Open CompleteWhiteBoxTestSuite.java
2. Right-click class name → "Run with Coverage"
3. Open Coverage tool window (View → Tool Windows → Coverage)
4. Review coverage percentages and highlighted code

## Command Line Options

# Run unit tests with coverage
./gradlew testDebugUnitTest --info

# View coverage report
open app/build/reports/tests/testDebugUnitTest/index.html

# Run instrumented tests (requires device)
./gradlew connectedDebugAndroidTest

## Coverage Metrics

- Line Coverage: Percentage of lines executed
- Branch Coverage: Percentage of branches taken  
- Method Coverage: Percentage of methods called

## Expected Coverage Areas

White-box tests cover:
- All models (Post, Comment, User, Prompt, Vote)
- Validators utility class

Black-box tests cover:
- All UI activities
- User interaction flows
