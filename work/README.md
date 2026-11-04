# Coursework code workspace

`Android-Project/` is the single buildable Android Studio project. Task folders below map assignments to implementation files without duplicating the Gradle project. Written answers are in the repository's `../tasks/` folders.

| Task folder | Runnable code |
|---|---|
| `Task-01-Splash-Screen-API/` | AndroidX system splash, Firebase App Startup hooks, debug LeakCanary dependency |
| `Task-02-Android-Fragments/` | UHC patient list/detail fragments, shared ViewModel, synthetic records, tablet detail pane |
| `Task-03-Fragments-Theory/` | Theory-only assignment; points to the runnable Fragment example |
| `Task-04-Fragments-Theory/` | Theory-only assignment; points to the runnable Fragment example |
| `Task-05-Material-Navigation-One/` | Material app bars, service navigation, issue form, tablet NavigationRail, WorkManager refresh hook |
| `Task-06-Material-Navigation-Two/` | EcoUganda destinations/drawer, report deep link and notification, tablet layout, App Startup hooks |
| `Task-07-ViewPager2-Theory/` | ViewPager2 + FragmentStateAdapter demonstration |

Open `Android-Project/` in Android Studio and run the `app` configuration. From a terminal, run `./gradlew assembleDebug`. Firebase and Maps need developer-supplied project configuration; API synchronization needs an authorized endpoint. The generated debug APK is at `Android-Project/app/build/outputs/apk/debug/app-debug.apk`.
