package com.example.coursework.screens;

public final class SettingsFragment extends CardScreenFragment {
    @Override protected String heading() { return "Settings"; }
    @Override protected String[] entries() { return new String[] {
        "Profile\nOfficer profile and district",
        "Language\nEnglish · Luganda · Runyankore",
        "Offline sync\nProject updates sync when a network is available"
    }; }
}
