package com.example.coursework.screens;

public final class ProjectsFragment extends CardScreenFragment {
    @Override protected String heading() { return "Community projects"; }
    @Override protected String[] entries() { return new String[] {
        "Solar: 12 active installations",
        "Hydro: 3 community projects",
        "Biogas: 8 school and household projects",
        "Map preview\nConnect the approved Maps SDK here when a Maps API key is configured."
    }; }
}
