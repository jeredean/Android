package com.example.coursework.screens;

public final class SchoolsFragment extends CardScreenFragment {
    @Override protected String heading() { return "Green Schools"; }
    @Override protected String[] entries() { return new String[] {
        "School projects\nView solar, tree planting, and waste reduction activities.",
        "Submit an update\nDistrict officers can record a school visit and attach a report."
    }; }
}
