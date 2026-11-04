# Task 6 — runnable code

Open [`../Android-Project`](../Android-Project) in Android Studio and run the `app` configuration. The drawer includes EcoUganda destinations; the toolbar notification action posts a local notification whose deep link opens a report ID.

- `app/src/main/res/navigation/main_nav_graph.xml` — NavHost destinations and `ecouganda://reports/{reportId}` deep link.
- `app/src/main/res/layout/activity_main.xml` and `layout-sw600dp/activity_main.xml` — phone drawer and tablet persistent-navigation layout.
- `app/src/main/res/menu/drawer_menu.xml` — Dashboard, Projects, Schools, Reports, and Settings.
- `app/src/main/java/com/example/coursework/screens/` — EcoUganda sample screens.
- `app/src/main/java/com/example/coursework/startup/` — App Startup initializers for Firebase when configured.

The Maps screen is a placeholder because a Maps API key was not supplied. The Firebase code is guarded; add your app's `google-services.json` to activate Analytics/Crashlytics. Performance target values in the answer sheet must be measured on the specified 1 GB device.
