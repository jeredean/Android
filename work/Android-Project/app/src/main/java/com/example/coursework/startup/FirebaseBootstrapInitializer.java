package com.example.coursework.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.google.firebase.FirebaseApp;

import java.util.Collections;
import java.util.List;

/** Initializes Firebase only when the developer has supplied google-services.json. */
public final class FirebaseBootstrapInitializer implements Initializer<Boolean> {
    @NonNull @Override public Boolean create(@NonNull Context context) {
        return FirebaseApp.initializeApp(context) != null;
    }
    @NonNull @Override public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
