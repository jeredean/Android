package com.example.coursework.screens;

import android.os.Bundle;

public final class ReportsFragment extends CardScreenFragment {
    @Override protected String heading() { return "District reports"; }
    @Override protected String[] entries() {
        String reportId = getArguments() == null ? "" : getArguments().getString("reportId", "");
        return new String[] {
            reportId.isEmpty() ? "No report opened from a notification" : "Opened report: " + reportId,
            "District summaries\nGreen Schools and project progress reports are available offline.",
            "Report status\nSubmitted reports synchronize when a network is available."
        };
    }
}
