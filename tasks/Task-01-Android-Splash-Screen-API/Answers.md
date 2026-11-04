# Task 1 — Android Splash Screen API

## Practical implementation and design

Use AndroidX `core-splashscreen` and call `installSplashScreen()` before `super.onCreate()`. Configure the starting theme with a solid background and a centered adaptive/vector icon, then set `postSplashScreenTheme` to the normal app theme. Keep startup work short; do not hold the system splash open for network calls. The icon should be an original or properly licensed vector/raster asset, legible at small sizes and with sufficient contrast. Android's splash API provides the launch transition, rather than a second custom splash Activity/layout.

## Memory-leak practical: recommended fix

Keep connectivity, NTP validation, token prefetch, and announcement download in a repository/WorkManager task, not in an Activity-retaining `AsyncTask`. Prefer cancellable coroutines or executor work with application-scoped dependencies. Observe results through a lifecycle-aware ViewModel. Register receivers/observers only for the needed lifecycle interval and unregister them; cancel requests and clear callbacks in `onDestroy`/`onStop` as appropriate. Avoid anonymous callbacks that capture an Activity; use weak/lifecycle-bound references where cancellation cannot guarantee release. Add LeakCanary only to debug builds and inspect retained-object traces after repeated launches. In CI, record leak reports and compare heap after a fixed repeated-launch scenario on a 1 GB profile; set an agreed baseline and fail on new retained Activities or sustained heap growth.

## Checkstyle practical

Use camelCase methods/fields, PascalCase types, and `UPPER_SNAKE_CASE` constants. Add Javadocs to public APIs, replace `5000` and `3` with named timeout/retry constants, cap lines at the requested 100 characters, and apply consistent indentation/whitespace. Put rules in versioned `checkstyle.xml`; run the same Checkstyle version/config locally and in CI. CI should fail on errors, report warnings, and retain the XML/HTML report. Example severity policy: errors for correctness or required naming/documentation violations; warnings for maintainability issues; info for suggestions; ignore disabled checks. Review the course rubric if its severity mapping is prescribed.

## Testing practical

Use fakes and dependency injection to cover online/offline startup and announcement parsing. Add instrumentation coverage for the launcher, accessibility labels, and startup navigation. A repeat-launch memory scenario should capture LeakCanary output and heap snapshots on the target low-memory profile. Firebase Test Lab can run the instrumentation APK on selected devices using `gcloud firebase test android run --app app-debug.apk --test app-debug-androidTest.apk`; pass `--environment-variables` or `--use-orchestrator` and instrumentation arguments such as `class` to select tests. Filter a test class with `-e class package.TestClass`; obtain coverage through the configured Android test coverage task/coverage artifact rather than assuming Test Lab produces it automatically.

For image loading, register a custom Espresso `IdlingResource` that becomes idle only when Glide/Coil work relevant to the screen completes; unregister it after each test. UIAutomator queries the accessibility/UI hierarchy, so it cannot reliably inspect inaccessible canvas content or a WebView DOM. Use UIAutomator for the WebView as a black-box user surface; Espresso-Web `onWebView()` can assert DOM elements and is usually more direct for web content, provided the WebView is debuggable/configured for the test.

## Theory: memory leaks

1. **UNEB e-Results at 1.5 million concurrent users:** on a 1 GB handset, a retained splash Activity consumes scarce heap, increases GC pauses, risks process death, and can prevent users from reaching results. At scale, the same defect creates widespread crashes and retries. Android Studio Memory Profiler can record heap dumps across repeated launches, inspect retained Activity instances and their reference paths, and compare heap after GC; allocation recording helps identify the source, while LeakCanary automates retained-object analysis.
2. **SafeBoda LocationManager callback:** LeakCanary reports a retained `SplashActivity` after it should be destroyed, with a reference path from `LocationManager`/listener (often the callback or its captured outer Activity) to the Activity. The trace identifies the GC root and fields keeping it reachable. Unregister the listener/cancel the location request at the lifecycle boundary.
3. **SharedPreferences context:** use `applicationContext` for a long-lived repository/helper because it survives Activity recreation without retaining an Activity/window. `this` inside an Activity is an Activity context; storing it in a singleton can retain the entire Activity. For a one-off UI-bound operation, Activity context is appropriate while its lifecycle is active.
4. **Activity vs Application context leak:** an Activity-context leak retains a destroyed screen, its views, and often a window (for example a splash callback or dialog retained by a singleton). An Application context is process-lifetime by design; retaining it does not retain an Activity, though an unbounded cache or other global resource can still waste memory. In UNEB startup, use app context for durable services/preferences, and lifecycle scope for screen callbacks.
5. **SafeBoda singleton cache:** do not accept/store an Activity context in a process-wide singleton. Prefer application context if genuinely required, or inject a scoped dependency. In Memory Profiler take a heap dump after destroying the screen, filter for the Activity, inspect “Path to GC Roots”/incoming references, and confirm whether the singleton/cache is the retaining path. Compare dumps before/after clearing the cache.

## Theory: Checkstyle and code quality

1. Consistent rules make code easier to read, review, and maintain across a team; they reduce style-only review discussions and catch preventable defects early. In a UNEB app, common naming, documentation, and formatting make urgent fixes safer across modules.
2. Checkstyle is a static source-code checker focused on coding conventions and configurable source patterns. It sits in the build/CI quality pipeline. Beyond whitespace it can check naming, imports, Javadocs, line length, modifiers, and selected structural rules; it is not a substitute for tests or a full semantic bug analyzer.
3. A **module** is a configured Checkstyle check in the checker tree. Five examples: `TreeWalker` (container for Java AST checks), `LineLength`, `JavadocType`, `MethodName`, and `AvoidStarImport` (the latter checks imports under TreeWalker).
4. Checkstyle mainly enforces style/conventions; PMD detects configurable code-quality patterns and likely defects; FindBugs' successor SpotBugs analyzes bytecode for bug patterns. They overlap in some rules but serve different emphases.
5. `checkstyle.xml` declares modules, properties, and severities so local builds and CI apply the same policy. Error blocks the build, warning reports a concern under a non-blocking policy, info is advisory, and ignore suppresses a check. The build integration determines whether warnings fail the build.

## Theory: UI components

1. `SwipeRefreshLayout` wraps a scrollable child and signals a pull-to-refresh gesture. Set its accent colors with `setColorSchemeResources`; the callback asks the ViewModel/repository to refresh and exposes loading state. Stop the indicator when the state completes, including errors, and avoid duplicate requests.
2. `EditText` accepts free text; `AutoCompleteTextView` suggests/selects one completion; `MultiAutoCompleteTextView` supports multiple tokenized completions with a `Tokenizer` (for example comma-separated recipients). Supply suggestions with a custom `ArrayAdapter` or a Material exposed dropdown menu and update options as data changes.
3. MotionLayout describes coordinated constraint-based transitions and supports progress, gestures, and keyframes. Compared with hand-managed `Animator`s, it centralizes layout states and transitions in a MotionScene, which is useful for interactive multi-view motion. Animator remains suitable for small property animations.
4. `ShapeableImageView` extends image display with a configurable Material shape model, enabling circle/rounded/cut-corner/diamond-like clipping and stroke color/width. Plain `ImageView` needs custom clipping/drawing for those shapes. Use a theme/shape resource and update stroke through its shape APIs when state changes.
5. `MaterialButton` supports Material theming, icon and `iconGravity`, stroke color/width, background tint, and corner radius in addition to button behavior. Prefer style/theme attributes or XML resources for a consistent family of controls; avoid setting a competing custom background drawable that overrides Material behavior.

## References

- [Android splash screen](https://developer.android.com/develop/ui/views/launch/splash-screen)
- [Android memory profiler](https://developer.android.com/studio/profile/memory-profiler)
- [LeakCanary](https://square.github.io/leakcanary/)
- [Espresso](https://developer.android.com/training/testing/espresso)
