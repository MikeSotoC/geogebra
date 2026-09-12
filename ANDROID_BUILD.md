# GeoGebra Android fork

This branch keeps upstream GeoGebra intact and adds a standalone Android wrapper in `android-wrapper/`.

## Current stage

The Android project is intentionally isolated from the upstream composite Gradle build. It opens GeoGebra Classic in an Android WebView, proving the APK shell independently of desktop builds.

## Build

Open `android-wrapper` as a project in Android Studio, install Android SDK 35, then build the `app` module.

Command line (with a compatible Gradle installation/wrapper):

```bash
cd android-wrapper
./gradlew assembleDebug
```

Expected APK path:

`app/build/outputs/apk/debug/app-debug.apk`

## Next milestone: offline APK

1. Build the required GeoGebra web distribution from `source/web` and its `source/shared` dependencies.
2. Copy the generated static application into `android-wrapper/app/src/main/assets/geogebra/`.
3. Change `MainActivity` to load `file:///android_asset/geogebra/index.html` (or the generated entry point).
4. Verify CAS, keyboard, file import/export, touch input and WebGL in Android WebView.

Do not remove `source/shared` or `source/web` yet: the current upstream architecture uses them to produce the application code that will be packaged into Android.
