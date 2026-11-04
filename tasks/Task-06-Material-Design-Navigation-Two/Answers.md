# Task 6 — Material Design Navigation Two

## EcoUganda practical design

`MainActivity` hosts a `NavHostFragment` inside a `DrawerLayout`; the `NavigationView` menu routes to Dashboard, Projects, Schools, Reports, and Settings destinations. Dashboard shows solar installations, acres reforested, and CO₂ metrics; Projects shows active solar/hydro/biogas work and a map; Schools covers Green Schools; Reports filters district/Green Schools reports; Settings contains profile, English/Luganda/Runyankore, and offline sync. Use Navigation Component destinations/actions so the drawer selection, fragment, and app-bar title stay synchronized. A notification deep link to Reports carries `reportId` and must work from cold start. On phones, use a hamburger drawer; on tablet widths (e.g. >600dp), use a permanently visible/locked-open navigation pane. Preserve current destination and arguments through rotation using Navigation saved state; do not manually recreate fragments if NavController restores them.

## Drawer theory

1. A Navigation Drawer is a panel listing primary destinations or navigation actions, typically opened from the app bar. Use it when the app has multiple top-level sections that do not fit in bottom navigation.
2. `DrawerLayout` is the parent coordinating main content and sliding drawer child; `NavigationView` is the drawer content/menu. Main content is the primary child; drawer is a sibling with `layout_gravity="start"`.
3. `start` means the leading edge for the current layout direction, so it appears on the left in LTR and right in RTL. `left` is physically left and breaks expected RTL mirroring.
4. Create the header XML and call `navigationView.inflateHeaderView(R.layout.nav_header)` after inflating the NavigationView/layout, typically during Activity setup (`onCreate`) and before accessing its header views. `addHeaderView()` is another API for an already-created header view. Avoid adding duplicate headers after recreation.
5. `app:itemTextColor` sets menu label state colors; `app:itemIconTint` sets icon tint. Use a state list (selected/disabled/default) tied to theme color roles for readable contrast and consistent Material theming.

## Startup optimization practical

Cold start is reported as 4.5 seconds on a 1 GB device, with Firebase Analytics, Crashlytics, Maps SDK, and local DB initialization in `Application.onCreate`, plus drawer/NavHost setup in `MainActivity`. First capture a repeatable baseline using `adb shell am start-activity -W -n <package>/<activity>` (same build/device, cold process) and Android Studio Startup/CPU profiler. Move nonessential map initialization until Projects is opened; initialize its SDK on demand if supported. Defer database opening/migrations until repository access, while keeping lightweight schema/state setup safe. Keep required crash reporting early only if product policy requires it; verify SDK-supported lazy/init options before changing Analytics/Crashlytics. Use AndroidX App Startup only to make initializer ordering/dependencies explicit, not as a magic speedup: it can still run work at startup. Measure each change separately, repeat cold starts, report median and range, and ensure deep links still route correctly.

| Variant | Cold start median | Range | Device/API | Notes |
|---|---:|---:|---|---|
| Baseline | measure | measure | record | same cold-start protocol |
| Deferred Maps | measure | measure | record | verify first map open |
| Deferred DB | measure | measure | record | verify first data request |

The brief asks for a reduction from 4.5 seconds to under 2.5 seconds and a component-by-component breakdown. Those are acceptance targets, not measured results in this answer sheet. Capture cold launches on the specified 1 GB device, repeat enough runs to report median/range, and time each initializer with Startup Profiler before reporting whether the target was met.

## Theory: Android app startup

1. A **cold start** creates the app process from scratch; a **warm start** reuses an existing process but must create/recreate an Activity; a **hot start** brings an existing Activity/task back to the foreground with little recreation. Optimizing `Application.onCreate()` most directly improves a cold start because that initialization runs in a newly created process; it can also help a warm start if the process had to be recreated, but not a hot resume.
2. For a Red Cross Uganda emergency app, the user tap asks Android to resolve the launcher entry, create the process, load app code/resources, run `Application` and Activity lifecycle callbacks, inflate/layout/draw the first frame, and schedule display. Process creation/class loading, synchronous SDK setup, disk I/O, database migrations, dependency graph work, and main-thread network waits are common expensive portions. Prioritize showing a useful first frame and defer work not needed for it.
3. Cold start means no process is running, so Android creates one and initializes Application/Activity. Warm start means the process remains but the Activity must be created again; it skips process creation and `Application.onCreate()`. Makindye Services users experience cold starts after the OS kills the process, a force-stop, reboot, or first launch; warm starts after navigating away while the process stays cached. Memory pressure, user force-stop, system lifecycle decisions, and task state determine the case.
4. Parents who open SMACK Parent Portal once or twice a day are likely to encounter a new process after the system reclaims it, so cold-start latency affects a large share of their meaningful sessions and first access to urgent school information. Warm starts resume faster because process initialization is already done. Track real-user cold/warm distributions and time-to-content rather than optimizing only a developer's repeated hot launch.
5. Slow startup adds friction and can increase abandonment, especially for time-sensitive daily tasks, so it is reasonable to treat startup as a retention risk and measure retention alongside latency. However, Google’s Android Vitals guidance defines a **slow cold startup** as 5 seconds or more; it does not publish a universal causal percentage linking each extra second to app retention. Therefore, don’t invent a retention statistic: compare the app’s cohort retention and startup percentiles before/after releases, controlling for other changes. The assignment’s 4.5-second baseline is close to Google's slow-start threshold and merits optimization. [Android Vitals startup guidance](https://developer.android.com/google/play/vitals/launch-time) and [Android app startup measurement](https://developer.android.com/topic/performance/measuring-performance).

## Testing practical: Firebase Test Lab

Build a test matrix for API 21–34 (or the assignment's available supported images), including low-memory 1 GB, mid-range, and high-end profiles plus phone and tablet dimensions. Use Firebase Test Lab instrumentation runs; add Robo exploration for broad smoke coverage. Verify drawer open/close, selected/highlighted item, title/destination, phone hamburger versus tablet permanent drawer, rotation restoration, `projectId`/`reportId` arguments, and notification deep links from cold start. Capture screenshots/logcat and test artifacts. Record device/API and failures; do not claim matrix results until run.

## Theory: test reliability/tools

1. `System.currentTimeMillis()` makes tests depend on wall-clock changes/time zones and is hard to control. Inject a `Clock` and use `Instant.now(clock)`; tests can provide a fixed clock for deterministic boundaries.
2. Use `WorkManagerTestInitHelper` to initialize WorkManager with a test configuration and `TestDriver` to mark constraints/delays as met or advance periods. Assert work state/output without waiting for real time; clean up test work.
3. Espresso `NoMatchingViewException` means the requested matcher found no view in the current hierarchy (wrong screen/ID, not yet rendered, hidden in another window, or timing issue). Check the failure hierarchy and current navigation state; wait for app idleness, verify exact resource ID/text, and ensure the view is displayed before action.
4. `System.getenv("EXTERNAL_STORAGE")` and `System.getenv("ANDROID_ART_ROOT")` read environment values when present; they can be absent/restricted and should not be assumed. Prefer Android APIs/context-specific directories for app storage; use environment variables only where the test environment contract requires them.
5. UiAutomator can inspect/toggle device network/battery settings when the test environment permits. Prefer controlled test preconditions, fake repositories, or emulator configuration; skip with an explicit assumption when a required system condition is unavailable, rather than making a flaky assertion based on uncontrolled battery/network state.

## Theory: UI/UX

1. Figma should map checkout/report submission states (empty, selected/cart, payment pending, confirmed) and transitions/errors so stakeholders can review the whole flow and labels before coding.
2. A Frame is a layout container with its own size, clipping, fill, and Auto Layout; a Group groups objects without the same independent layout behavior. Frame boundaries and Auto Layout make padding/spacing clearer in handoff; margin is usually represented by spacing between frames/elements.
3. Prototypes cannot fully reproduce native keyboard, status bar, permission dialogs, or platform back behavior. Annotate expected native behavior and build a device prototype/test for those states.
4. Adobe XD local/shared styles keep Material palette and typography consistent across artboards; update the shared definition once and review linked screens for unintended changes.
5. Design skeleton loading states with the same content geometry, shimmer/placeholder contrast, and reduced-motion considerations. In Android, translate with a skeleton/shimmer component or static placeholders, announce loading accessibly, and remove it when actual content arrives.

