# Task 5 — Material Design Navigation One

## UgandaConnect practical design

Build a shared top app bar with the current destination title (Solar Subsidies, My Results, Bill Payments), drawer/menu navigation, search, and a notification icon with an unread badge. Use a scroll behavior that collapses on downward content scroll and reappears on upward scroll. On phones, provide a bottom app bar with 3–5 primary destinations (Home, Services, Bookmarks, Profile) and a FAB for a contextual action such as reporting an issue; hide it when the keyboard covers the content and restore it afterward. On widths above the phone breakpoint (for example `sw600dp`), use a NavigationRail or responsive navigation pattern rather than forcing a bottom bar across a tablet/desktop canvas. Use Uganda's black/yellow/red with service accents (REA green, health blue, education yellow) while maintaining contrast and semantic state cues.

## Theory: app bars/navigation

1. The two common app bars here are Top App Bar (screen identity and actions) and Bottom App Bar (primary navigation/actions near a phone user's thumb, often with a FAB).
2. Material 3 top app bar variants: **small** for standard/simple screens; **center-aligned** when centered title hierarchy fits; **medium** for stronger title emphasis with moderate scroll expansion; **large** for prominent title on top-level screens and collapsing behavior. Choose based on hierarchy and available space.
3. A Material Top App Bar is a Material component/pattern that supplies roles, actions, scroll behavior, and theme integration; legacy `Toolbar` is a lower-level AndroidX widget often used as an app bar. Choose the M3 component when the app uses Material 3 styling/behavior and wants consistent built-in patterns; Toolbar remains useful for custom/legacy setups.
4. Bottom app bars suit compact phone screens and reachable primary actions. On tablets/desktops, the width and pointer/keyboard use favor a navigation rail, permanent drawer, or navigation pane, avoiding a bar stretched across a large display.
5. Bottom navigation should usually show 3–5 top-level destinations. More items become crowded, labels/icons become harder to scan, and touch targets shrink; consolidate secondary destinations under Services/More or a drawer.

## Battery optimization practical

Replace 15-minute exact/repeating `AlarmManager` polls for REA status, appointments, UNEB results, and utility payment status with WorkManager for deferrable sync. A periodic request has a 15-minute minimum interval; use a longer interval such as 4 hours if product freshness allows. Add constraints such as connected network and battery-not-low; use expedited work only for genuinely time-critical user-initiated work (expedited work is quota-limited and is not a general replacement for periodic polling). Respect Doze/standby and use server push for urgent updates where available. Compare baseline and changed behavior in Android Studio Energy Profiler across the same duration/network/device and record wakeups, jobs, and battery drain.

## Theory: WebP

1. WebP can reduce storage/transfer compared with PNG for many assets. Lossy mode trades detail for size; lossless preserves pixels. Decode speed is device/library/image-dependent, so measure it.
2. In Android Studio, select an image and invoke Convert to WebP, choose lossless/lossy and quality, preview and save, then update references and inspect. For MTN MyZone's 80% goal, compute total converted bytes against original bytes and review quality; that target is not guaranteed.
3. Smaller Mulago Patient Tracker images can reduce transfer time and mobile data. Approximate reduction is `100 × (1 - compressed_bytes/original_bytes)%`; measure actual assets and network conditions.
4. Check WebP decoding across the app's minSdk and image loading stack. Modern Android supports it, but legacy API/device paths may differ; keep fallback formats where required by supported devices/tooling.
5. APK Analyzer measures packaged asset contribution and APK/AAB size. For image quality use visual inspection at target density, a pixel/structural comparison tool such as SSIM when suitable, and decode/render timing on target hardware.

## Theory: TDD/testing

1. Red-Green-Refactor: write a failing test, implement the smallest change to pass, then improve design while keeping tests green. A ViewModel test can assert initial state, invoke a fake repository result, and assert loading then success/error state.
2. `AndroidJUnit4` is a JUnit runner that integrates Android instrumentation execution, test discovery, and lifecycle/runtime setup; it enables Android-dependent tests and reporting on device/emulator.
3. Avoid `Thread.sleep()`: expose animation completion with an Espresso `IdlingResource` or test a stable state after animator completion. For deterministic screenshot/manual runs, developer animation scale settings can disable/shorten animations, but should not be the only synchronization technique.
4. Firebase Test Lab runs instrumentation tests on virtual or physical devices; Robo test explores the app automatically without a hand-written test. Collect logcat/test artifacts and investigate ANR traces/stack rather than treating an ANR as a generic test failure.
5. UiAutomator can locate external permission dialogs using `By.text(...)`, `By.res(...)`, or `UiSelector().text(...)`/`resourceId(...)`, then click the expected choice. Handle API/OEM wording variations and avoid granting more permission than the test needs.

## Theory: design tools

1. Figma Auto Layout arranges/resizes children with spacing, padding, and alignment rules; Adobe XD responsive resize/constraints control adaptation. Use constraints and dp-equivalent dimensions/aspect ratios so cards/forms adapt to phone/tablet frames, then check long translated labels.
2. A component (XD symbol) is a reusable design element. Material styles/tokens define type, color, elevation, and shape; component variants represent states/size options, while instance overrides should be intentional and limited.
3. A high-fidelity interactive prototype links screens and models taps/swipes/keyboard-visible states, helping validate UgandaConnect user flows before native code. It cannot fully reproduce native OS behavior; annotate keyboard, status bar, permissions, and edge cases.
4. Handoff/inspect panels communicate dimensions in dp, color hex values, typography, spacing, and density-specific assets. Include target Android density buckets and exported asset formats/naming.
5. Collaborate with shared libraries/components and branches or named versions; agree on ownership, review changes, and publish updates so multiple designers/developers do not silently overwrite the system.

## Performance practical: JMeter plan

Create a non-production test plan for 500,000 peak concurrent virtual users only after the backend owner supplies capacity limits and a safe staging environment. Model personas for REA, public health, UNEB, UMEME/NWSC, and KCCA. Journeys: authenticate, open module, search/list, view detail, submit a form/payment-status request, and log out. Parameterize accounts, tokens, IDs, and think time; use realistic ramp-up/hold/ramp-down, correlation, and data variation. Add HTTP assertions and avoid reusing one session if production behavior creates unique sessions. Collect per-operation throughput/RPS, average, p95/p99 latency, error rate, and server CPU/memory/DB saturation. Run a small validation first, then staged load; never aim this test at production.

| Operation | RPS | Avg | p95 | p99 | Error % |
|---|---:|---:|---:|---:|---:|
| Login | measure | measure | measure | measure | measure |
| Module/search/detail | measure | measure | measure | measure | measure |
| Submit | measure | measure | measure | measure | measure |

