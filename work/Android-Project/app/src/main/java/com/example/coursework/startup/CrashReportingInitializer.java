package com.example.coursework.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Collections;
import java.util.List;

/** Enables Crashlytics only when Firebase is configured for this app. */
public final class CrashReportingInitializer implements Initializer<Boolean> {
    @NonNull @Override public Boolean create(@NonNull Context context) {
        if (FirebaseApp.getApps(context).isEmpty()) return false;
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        return true;
    }
    @NonNull @Override public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.singletonList(FirebaseBootstrapInitializer.class);
    }
}
