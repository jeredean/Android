package com.example.coursework.screens;

public final class DashboardFragment extends CardScreenFragment {
    @Override protected String heading() { return null; }
    @Override protected String[] entries() { return new String[] {
        "Solar installations\n248 community systems installed",
        "Acres reforested\n1,420 acres reported by district teams",
        "CO₂ avoided\n860 tonnes estimated from submitted project data"
    }; }
}
