package com.example.uhc

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), PatientListFragment.OnPatientSelectedListener {

    private val isTablet: Boolean
        get() = findViewById<View>(R.id.detail_container) != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarForCurrentScreen()
        }

        if (savedInstanceState == null) {
            if (isTablet) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.list_container, PatientListFragment())
                    .add(R.id.detail_container, PatientDetailFragment.newInstance(null))
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, PatientListFragment())
                    .commit()
            }
        } else {
            updateToolbarForCurrentScreen()
        }
    }

    override fun onPatientSelected(patient: Patient) {
        if (isTablet) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.detail_container, PatientDetailFragment.newInstance(patient.id))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, PatientDetailFragment.newInstance(patient.id))
                .addToBackStack(patient.id)
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(!isTablet)
        supportActionBar?.title = if (isTablet) {
            getString(R.string.patient_list_title)
        } else {
            patient.name
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!isTablet && supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return true
        }
        return super.onSupportNavigateUp()
    }

    private fun updateToolbarForCurrentScreen() {
        val showingDetail = supportFragmentManager.findFragmentById(
            if (isTablet) R.id.detail_container else R.id.fragment_container
        ) is PatientDetailFragment
        supportActionBar?.setDisplayHomeAsUpEnabled(showingDetail && !isTablet)
        supportActionBar?.title = if (showingDetail && !isTablet) {
            getString(R.string.patient_detail_title)
        } else {
            getString(R.string.app_name)
        }
    }
}
