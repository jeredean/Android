# Task 7 — ViewPager2 theory

Runnable ViewPager2 example: see `../../work/Task-07-ViewPager2-Theory/` and open `../../work/Android-Project/` in Android Studio.

1. ViewPager2 is a UI component for paging through content, usually horizontally (and optionally vertically), with swipe gestures and programmatic page selection. It suits a guided sequence, tabs, or image/content pages.
2. Fragments provide each page's independent UI and lifecycle when pages are complex or need their own state. A pager adapter creates the page fragments and ViewPager2 coordinates their display; pages should not assume all neighboring fragments are simultaneously active.
3. `FragmentStateAdapter` supplies fragment pages and manages their saved state/lifecycle as pages enter and leave the active window. Implement `getItemCount()` and `createFragment(position)`; give stable item IDs when the collection can change.
4. Add ViewPager2 to the layout, create a `FragmentStateAdapter` tied to the host lifecycle/FragmentManager, implement count and fragment creation, set the adapter, then connect optional `TabLayoutMediator`/page indicators and handle page changes. Preserve page selection where needed and test rotation/back navigation.
5. ViewPager2 with Fragments offers intuitive swipe navigation, modular page code and reuse, and lifecycle/state management for pages. It also supports vertical orientation and improved RTL/accessibility behavior compared with the legacy ViewPager; use it only when paging is a clear fit, not as a substitute for general app navigation.

## Attached starter files

The assignment also lists home Activity XML, Java Activity logic, and “Material Design Content Display One” text attachments. Their contents were not included in the assignment description captured here; the answers above cover the five theory questions posted with Task 7.
