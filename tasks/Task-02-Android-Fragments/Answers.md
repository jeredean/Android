# Task 2 — Android Fragments (UHC)

## Practical design

For Uganda Health Connect, host `PatientListFragment` and `PatientDetailFragment` in one `MainActivity`. On phones (5–6 inch class), show the list and replace it with details when a patient is selected; Back returns to the list. On `sw600dp` tablets use a two-pane layout, keeping the list and selected detail visible. Pass a patient ID, not a whole mutable patient object. Use synthetic/demo records in examples; do not put real patient details in source or screenshots.

### Communication

Preferred: both fragments obtain an Activity-scoped `PatientSelectionViewModel` with `new ViewModelProvider(requireActivity())...`; the list sets `selectedPatientId`, and the detail observes it with `getViewLifecycleOwner()`. For a one-shot result, use Fragment Result API (`setFragmentResult` / `setFragmentResultListener`). If an interface callback is required, define a host interface, have the Activity implement it, cast in `onAttach`, and clear the callback in `onDetach`; avoid retaining an Activity field beyond attachment.

### Large-list image optimization

Load patient thumbnails through Glide/Coil in `onBindViewHolder`, request a thumbnail-sized decode (`override`/size), use placeholders, and clear the request when a holder is recycled. Set a bounded memory/disk cache appropriate to the app. For full-resolution medical imagery, preserve source quality and use an explicit detail viewer; do not lossy-compress diagnostic content without clinical approval. Convert only non-diagnostic decorative assets to WebP, compare source and output file sizes, and inspect visual quality. Measure APK size with APK Analyzer and heap during rapid scroll with Android Studio Profiler on a 1 GB profile. Record before/after; do not claim the 80 MB target until measured.

| Measurement | Before | After | Method/device |
|---|---:|---:|---|
| APK size | measure | measure | APK Analyzer, same build variant |
| peak/steady heap during scroll | measure | measure | Profiler, same dataset and scroll script |
| frame jank | measure | measure | Profiler/system trace |

### Lint practical

Clear view binding/references in `onDestroyView`; use `requireContext()` only where attachment is guaranteed, otherwise handle nullable `context`/`activity`. Do not commit transactions after state has been saved; defer navigation or use state-loss commit only when losing the operation is explicitly acceptable. Avoid repeated fragment recreation and use stable navigation/state. Enable Android Lint in Gradle/CI and review fragment lifecycle, nullability, and transaction warnings. `FragmentContainerView` is the recommended container for fragment content.

## Theory: core fragments

1. A Fragment sends the selected item ID to its host through a shared Activity-scoped ViewModel, observable by the detail fragment. Casting/storing a concrete Activity couples the fragment to that host and can retain a stale Activity after recreation. A shared ViewModel is lifecycle-aware and survives configuration changes.
2. Interface option: `interface OnPatientSelectedListener { void onPatientSelected(String patientId); }`. In `onAttach(Context context)`, verify/cast the host; in `onViewCreated`, call `listener.onPatientSelected(id)`; clear the reference in `onDetach`. Prefer the shared ViewModel for this screen's shared state.
3. Both fragments use `new ViewModelProvider(requireActivity()).get(SelectionViewModel.class)`. The list calls `viewModel.select(id)`; the detail observes `viewModel.getSelectedId()` with its view lifecycle owner and loads the corresponding repository record.
4. `setTargetFragment()` and `onActivityResult()` are deprecated, couple fragments through target/request codes, and make lifecycle/state flows harder to reason about. Shared ViewModel works well for ongoing shared state; Fragment Result API is lifecycle-aware for one-time bundles without direct references.
5. Register `setFragmentResultListener(REQUEST_KEY, getViewLifecycleOwner(), (key, bundle) -> ...)` in the receiving fragment; sender calls `setFragmentResult(REQUEST_KEY, bundle)`. Result delivery is associated with the FragmentManager and waits until the listener is at least STARTED. Use the same manager/key scope on both sides.

## Theory: image optimization

1. PNG→WebP often reduces photographic/complex icon storage; lossy WebP trades fidelity for size while lossless WebP preserves pixels. Decode cost depends on dimensions, format, device/API, and library, so measure decode and visual quality rather than assuming it is faster. Keep transparency/edge quality acceptable for UWA Spotter icons.
2. In Android Studio, open the image in Project view, choose **Convert to WebP** (or right-click supported raster assets), select lossy/lossless and quality, preview, then review the generated file and references. For MTN MyZone's 80% target, batch convert copies, compare total bytes and representative visual details; do not overwrite originals until reviewed.
3. A smaller file can reduce Mulago Patient Tracker transfer time/data use, especially on mobile networks. The reduction is `100 × (1 − output_bytes / input_bytes)%`; it varies widely by image and settings, so report measured aggregate and per-image values, not a generic assumed percentage.
4. WebP support depends on Android version and decoder/library path. Current Android releases decode WebP; for older supported devices verify the minimum API and chosen image library/Android platform behavior. Supply a compatible fallback or keep PNG/JPEG where required by the minSdk and toolchain.
5. APK Analyzer reports packaged file sizes and contribution to APK/AAB; it does not judge fidelity. Use a side-by-side visual review at target display size, image-diff/SSIM or another objective quality metric where useful, plus decode/render timing on representative devices.

## Theory: testing

1. `MockitoAnnotations.openMocks(this)` initializes `@Mock` fields and returns an `AutoCloseable` to close in teardown. Modern Mockito supports mocking final classes/methods when the inline mock maker is enabled (for newer Mockito versions it is commonly the default; older setups add `mockito-inline`). Verify the dependency/version and avoid mocking Android framework behavior unnecessarily.
2. `@Before` prepares each instrumentation test and `@After` releases resources, resets state, unregisters listeners, and closes files/databases. Android Test Orchestrator runs tests in separate instrumentation invocations/processes, improving isolation; cleanup is still needed for external resources and shared device state.
3. Espresso-Web uses `onWebView()`; enable JavaScript if the page requires it, then use `withElement(findElement(Locator.ID, "username"))` and assert text/attributes. Synchronize page readiness and avoid arbitrary sleeps.
4. Firebase Test Lab virtual devices provide scalable API/configuration coverage; physical devices better represent real sensors, hardware, and OEM behavior. Mock/fake location and sensor inputs for deterministic logic tests, then use physical coverage for sensor integration and permissions.
5. UiAutomator can capture a screenshot (`UiDevice.takeScreenshot`) and save it to test artifacts/storage. Name it with test/device/build metadata, attach/pull the artifact from the runner, and avoid capturing real patient data.

## Theory: UI/UX

1. Lottie renders authored vector animation in a Material loading state and can feel more polished than a static spinner. Keep it lightweight, respect reduced-motion/accessibility expectations, and provide a textual loading state; animation must not imply progress that is unknown.
2. Palette extracts prominent colors from a bitmap for coordinated accents. Apply a selected color to card/toolbar/status-bar only after checking contrast and brand/accessibility rules; use a stable fallback while extraction runs.
3. Spinner is a compact native selection control; an exposed dropdown `AutoCompleteTextView` in `TextInputLayout` is searchable/stylable and better for longer lists. Choose based on list length and need for typing, and label the field.
4. A stepper shows progress through stages. Persist current step and entered values in a ViewModel/saved state; validate each stage before advancing, allow review/confirmation, and restore after rotation/process recreation.
5. A floating label belongs inside the `TextInputLayout` visual boundary; a label floating outside it can appear detached and weakens field association. Use the component's hint/floating-label behavior and animate through supported Material states rather than a separate label that loses accessibility linkage.

## References

- [Fragments](https://developer.android.com/guide/fragments)
- [Fragment communication](https://developer.android.com/guide/fragments/communicate)
- [Android image formats](https://developer.android.com/guide/topics/media/media-formats)
- [Firebase Test Lab](https://firebase.google.com/docs/test-lab/android/command-line)
