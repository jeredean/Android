package com.example.coursework.screens;

public final class UgandaConnectFragment extends CardScreenFragment {
    @Override protected String heading() { return "UgandaConnect services"; }
    @Override protected String[] entries() { return new String[] {
        "Solar Subsidies\nREA application status and community installations",
        "My Results\nUNEB examination results and term dates",
        "Hospital Services\nAppointments and results",
        "Bill Payments\nUMEME and NWSC account status",
        "KCCA Reports\nReport a city service issue",
        "Bookmarks\nSave frequently used public services"
    }; }
}
