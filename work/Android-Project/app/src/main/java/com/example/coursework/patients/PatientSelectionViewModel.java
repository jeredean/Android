package com.example.coursework.patients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/** Activity-scoped selection state shared by the patient list and detail fragments. */
public final class PatientSelectionViewModel extends ViewModel {
    private final MutableLiveData<String> selectedPatientId = new MutableLiveData<>();
    public LiveData<String> getSelectedPatientId() { return selectedPatientId; }
    public void select(String patientId) { selectedPatientId.setValue(patientId); }
}
