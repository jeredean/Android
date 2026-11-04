# Task 1 — runnable code

Open [`../Android-Project`](../Android-Project) in Android Studio and run the `app` configuration.

- `app/src/main/java/com/example/coursework/SplashActivity.java` — AndroidX system splash and transition to the app.
- `app/src/main/res/values/themes.xml`, `drawable/ic_splash.xml`, and `AndroidManifest.xml` — launcher theme, icon, and entry point.
- `app/src/main/java/com/example/coursework/startup/` — AndroidX App Startup bootstrap for Analytics and Crashlytics. Add your own `google-services.json` to enable Firebase; without it the app still launches.
- `app/build.gradle.kts` — debug-only LeakCanary.

No Activity-retaining callbacks or network work run during splash. The posted brief's NTP/auth/announcement services need real endpoints and credentials, which are not part of the Classroom prompt.
