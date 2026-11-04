package com.example.coursework;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.android.material.navigation.NavigationView;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/** Hosts the task demos: UHC fragments, UgandaConnect navigation, and EcoUganda drawer. */
public final class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "ecouganda_reports";
    private NavController navController;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView drawer;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private NavigationRailView navigationRail;
    private final ActivityResultLauncher<String> notificationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) sendReportNotification("UG-2026-001");
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.top_app_bar);
        drawer = findViewById(R.id.navigation_view);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        fab = findViewById(R.id.main_fab);
        navigationRail = findViewById(R.id.navigation_rail);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.primary_nav_host);
        if (host == null) throw new IllegalStateException("NavHostFragment missing");
        navController = host.getNavController();
        Set<Integer> topLevelDestinations = new HashSet<>(Arrays.asList(
                R.id.eco_dashboard, R.id.uganda_connect, R.id.patient_list,
                R.id.eco_projects, R.id.eco_schools, R.id.eco_reports, R.id.eco_settings));
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .setOpenableLayout(drawerLayout)
                .build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(drawer, navController);
        if (bottomAppBar != null) setupPhoneNavigation();
        if (navigationRail != null) setupTabletNavigation();

        toolbar.inflateMenu(R.menu.top_app_bar);
        com.google.android.material.badge.BadgeDrawable badge =
                com.google.android.material.badge.BadgeDrawable.create(this);
        badge.setNumber(3);
        com.google.android.material.badge.BadgeUtils.attachBadgeDrawable(
                badge, toolbar, R.id.action_notifications);
        toolbar.setOnMenuItemClickListener(this::onToolbarItemSelected);
        if (fab != null) fab.setOnClickListener(view -> openIssueReport());
        configureResponsiveChrome();
        createNotificationChannel();
        scheduleOfflineRefresh();

    }

    private void setupPhoneNavigation() {
        Menu menu = bottomAppBar.getMenu();
        for (int i = 0; i < menu.size(); i++) menu.getItem(i).setCheckable(true);
        bottomAppBar.setOnMenuItemClickListener(item -> navigateTopLevel(item.getItemId()));
        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            MenuItem item = bottomAppBar.getMenu().findItem(destination.getId());
            if (item != null) item.setChecked(true);
        });
    }

    private void setupTabletNavigation() {
        NavigationUI.setupWithNavController(navigationRail, navController);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN,
                androidx.core.view.GravityCompat.START);
    }

    private boolean navigateTopLevel(int destinationId) {
        if (destinationId == navController.getCurrentDestination().getId()) return true;
        NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(R.id.eco_dashboard, false, true)
                .build();
        navController.navigate(destinationId, null, options);
        return true;
    }

    private boolean onToolbarItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            navController.navigate(R.id.eco_projects);
            return true;
        }
        if (item.getItemId() == R.id.action_notifications) {
            if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                sendReportNotification("UG-2026-001");
            }
            return true;
        }
        return false;
    }

    private void openIssueReport() {
        if (navController.getCurrentDestination().getId() != R.id.report_form) {
            navController.navigate(R.id.report_form);
        }
    }

    private void configureResponsiveChrome() {
        View root = findViewById(R.id.root_content);
        View appBar = findViewById(R.id.app_bar_layout);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            if (appBar != null) {
                appBar.setPadding(0, topInset, 0, 0);
            }
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            if (bottomAppBar != null) {
                bottomAppBar.setVisibility(imeVisible ? View.GONE : View.VISIBLE);
                if (fab != null) fab.setVisibility(imeVisible ? View.GONE : View.VISIBLE);
            }
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "EcoUganda reports", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Updates for environmental reports");
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    private void sendReportNotification(String reportId) {
        Uri uri = Uri.parse("ecouganda://reports/" + Uri.encode(reportId));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri, this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, reportId.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_layers)
                .setContentTitle("EcoUganda report updated")
                .setContentText("Tap to view report " + reportId)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        try {
            NotificationManagerCompat.from(this).notify(reportId.hashCode(), notification);
        } catch (SecurityException ignored) {
            // Android 13+: grant POST_NOTIFICATIONS to show notifications.
        }
    }

    private void scheduleOfflineRefresh() {
        androidx.work.Constraints constraints = new androidx.work.Constraints.Builder()
                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        androidx.work.PeriodicWorkRequest request =
                new androidx.work.PeriodicWorkRequest.Builder(
                        com.example.coursework.sync.ProjectRefreshWorker.class,
                        4, java.util.concurrent.TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();
        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "ecouganda-project-refresh",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP, request);
    }
}
