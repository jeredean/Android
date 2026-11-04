package com.example.coursework;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumentation tests: these run on an emulator/device and drive the real UI of MainActivity.
 * Run with: ./gradlew :app:connectedDebugAndroidTest
 * (Phone layout assumed: the FAB and drawer are part of the phone UI.)
 */
@RunWith(AndroidJUnit4.class)
public class AppInstrumentedTest {

    private static int currentDestinationId(ActivityScenario<MainActivity> scenario) {
        AtomicInteger id = new AtomicInteger(-1);
        scenario.onActivity(activity -> {
            NavController nav = Navigation.findNavController(activity, R.id.primary_nav_host);
            id.set(nav.getCurrentDestination().getId());
        });
        return id.get();
    }

    @Test
    public void appContext_hasCorrectPackageName() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.coursework", appContext.getPackageName());
    }

    @Test
    public void launch_showsToolbarAndStartsOnDashboard() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.top_app_bar)).check(matches(isDisplayed()));
            assertEquals(R.id.eco_dashboard, currentDestinationId(scenario));
        }
    }

    @Test
    public void fab_opensReportForm() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.main_fab)).perform(click());

            assertEquals(R.id.report_form, currentDestinationId(scenario));
        }
    }

    @Test
    public void drawer_projectsItem_navigatesToProjects() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START, false);
            });

            onView(allOf(withText("Projects"), isDescendantOfA(withId(R.id.navigation_view))))
                    .perform(click());

            assertEquals(R.id.eco_projects, currentDestinationId(scenario));
        }
    }

    @Test
    public void deepLink_opensReportsWithReportId() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("ecouganda://reports/UG-2026-001"), context, MainActivity.class);

        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(intent)) {
            AtomicReference<String> reportId = new AtomicReference<>();
            scenario.onActivity(activity -> {
                NavController nav = Navigation.findNavController(activity, R.id.primary_nav_host);
                reportId.set(nav.getCurrentBackStackEntry().getArguments().getString("reportId"));
            });

            assertEquals(R.id.eco_reports, currentDestinationId(scenario));
            assertEquals("UG-2026-001", reportId.get());
        }
    }
}
