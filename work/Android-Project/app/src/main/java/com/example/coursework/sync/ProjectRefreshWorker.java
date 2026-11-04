package com.example.coursework.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/** Periodic offline refresh hook; replace the sample body with an authenticated repository call. */
public final class ProjectRefreshWorker extends Worker {
    public ProjectRefreshWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull @Override public Result doWork() {
        if (isStopped()) return Result.retry();
        // The coursework project has no backend endpoint or credentials; this is a safe local demo.
        return Result.success();
    }
}
