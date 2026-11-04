# Task 4 — Fragment theory

Runnable Fragment examples are in `../../work/Task-02-Android-Fragments/` and the Android project at `../../work/Android-Project/`.

1. For host communication, use a shared ViewModel for ongoing shared state or Fragment Result API for one-time results. Both are lifecycle-aware; direct fragment-to-fragment references/casts couple screens and can point at destroyed instances.
2. Static XML fragments/containers are easy to inspect, preview, and use for fixed layouts, but are less flexible when screens vary by runtime data/window size. Dynamic transactions adapt to runtime navigation and layouts, but require careful lifecycle, state restoration, and back-stack handling.
3. Use resource qualifiers such as `layout-sw600dp` for tablet layouts. Phones can show a list then detail; tablets can keep both master and detail panes visible. Share the selected item ID/state so both layouts use the same data flow.
4. On rotation, Android recreates the Activity and restores FragmentManager state. Save small UI state with `onSaveInstanceState`; keep screen data in a ViewModel/repository and use SavedStateHandle for process recreation. `setRetainInstance()` is deprecated; ViewModel is the preferred configuration-change holder.
5. `DialogFragment` manages modal dialogs through FragmentManager/lifecycle; legacy `ListFragment` supplies a list-centric UI and is less flexible than RecyclerView; `PreferenceFragmentCompat` manages preference screens from AndroidX Preference. Prefer `androidx.fragment.app` over the old framework fragment package, and use `FragmentContainerView` as the XML host because it is designed for FragmentManager integration and transitions.
