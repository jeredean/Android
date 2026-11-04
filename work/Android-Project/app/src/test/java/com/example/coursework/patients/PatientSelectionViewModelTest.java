package com.example.coursework.patients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Local JVM unit tests for {@link PatientSelectionViewModel}.
 * Run with: ./gradlew :app:testDebugUnitTest
 */
public class PatientSelectionViewModelTest {

    // Makes LiveData run synchronously on the JVM (no Android main looper needed).
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private PatientSelectionViewModel viewModel;
    private final List<String> received = new ArrayList<>();
    private final Observer<String> observer = received::add;

    @Before
    public void setUp() {
        viewModel = new PatientSelectionViewModel();
    }

    @After
    public void tearDown() {
        viewModel.getSelectedPatientId().removeObserver(observer);
        received.clear();
    }

    @Test
    public void initialSelection_isNull() {
        assertNull(viewModel.getSelectedPatientId().getValue());
    }

    @Test
    public void select_publishesPatientId() {
        viewModel.select("P-001");

        assertEquals("P-001", viewModel.getSelectedPatientId().getValue());
    }

    @Test
    public void select_replacesPreviousPatient() {
        viewModel.select("P-001");
        viewModel.select("P-002");

        assertEquals("P-002", viewModel.getSelectedPatientId().getValue());
    }

    @Test
    public void select_notifiesActiveObserver() {
        viewModel.getSelectedPatientId().observeForever(observer);

        viewModel.select("P-001");
        viewModel.select("P-002");

        assertEquals(2, received.size());
        assertEquals("P-001", received.get(0));
        assertEquals("P-002", received.get(1));
    }

    @Test
    public void lateObserver_receivesLatestSelection() {
        viewModel.select("P-003");

        viewModel.getSelectedPatientId().observeForever(observer);

        assertEquals(1, received.size());
        assertEquals("P-003", received.get(0));
    }

    @Test
    public void select_null_clearsSelection() {
        viewModel.select("P-001");
        viewModel.select(null);

        assertNull(viewModel.getSelectedPatientId().getValue());
    }
}
