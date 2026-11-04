package com.example.coursework.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collections;
import java.util.List;

/** Keep only required analytics startup here; log no personal or health information. */
public final class AnalyticsInitializer implements Initializer<Boolean> {
    @NonNull @Override public Boolean create(@NonNull Context context) {
        if (FirebaseApp.getApps(context).isEmpty()) return false;
        FirebaseAnalytics.getInstance(context);
        return true;
    }
    @NonNull @Override public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.singletonList(FirebaseBootstrapInitializer.class);
    }
}
