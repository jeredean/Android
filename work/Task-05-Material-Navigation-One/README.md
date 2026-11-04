# Task 5 — runnable code

Open [`../Android-Project`](../Android-Project) in Android Studio and run the `app` configuration. Choose **Services** or tap the report FAB.

- `app/src/main/res/layout/activity_main.xml` — MaterialToolbar, collapsing app bar, BottomAppBar with four destinations, and docked FAB.
- `app/src/main/res/layout-sw600dp/activity_main.xml` — tablet NavigationRail layout.
- `app/src/main/res/menu/top_app_bar.xml` and `bottom_app_bar.xml` — search, notification, and primary destinations.
- `app/src/main/java/com/example/coursework/MainActivity.java` — route handling, notification deep link demo, IME-aware bottom bar, and WorkManager schedule.
- `app/src/main/java/com/example/coursework/sync/ProjectRefreshWorker.java` — connected/battery-aware four-hour sample background task.
- `app/src/main/java/com/example/coursework/screens/UgandaConnectFragment.java` — REA, UNEB, health, utilities, and KCCA sample service cards.

The worker is a safe no-backend placeholder; connect it to an authorized staging API before syncing live data. JMeter load execution remains a separate staging task.
