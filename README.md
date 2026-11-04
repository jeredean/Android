# Android

Intermediate Mobile Programming coursework: written answers and a runnable Android application demonstrating the assigned topics.

## Contents

- [`tasks/`](tasks/): coursework answer sheets.
- [`work/`](work/): Android Studio project and task-by-task implementation notes.
- [`work/Android-Project/`](work/Android-Project/): the runnable Android app.

## Build and test

```sh
cd work/Android-Project
./gradlew assembleDebug
./gradlew clean test
```

The local JVM test suite runs without a device. Instrumentation tests require an Android emulator or connected device. Firebase and map/network features require developer-provided configuration and credentials; local configuration files are excluded from version control.
