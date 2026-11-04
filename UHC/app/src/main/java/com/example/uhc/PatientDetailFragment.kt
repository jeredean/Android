package com.example.uhc

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PatientDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val patient = PatientRepository.findById(arguments?.getString(ARG_PATIENT_ID))
        val scrollView = ScrollView(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.surface))
            isFillViewport = true
        }
        val content = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(24), dp(24), dp(32))
        }

        if (patient == null) {
            content.addView(text(context, getString(R.string.select_patient_title), 24f, true))
            content.addView(text(context, getString(R.string.select_patient_message), 15f, false).apply {
                setPadding(0, dp(10), 0, 0)
            })
            scrollView.addView(content)
            return scrollView
        }

        content.addView(text(context, getString(R.string.patient_detail_title), 14f, true, R.color.uhc_green))
        content.addView(text(context, patient.name, 28f, true).apply {
            setPadding(0, dp(8), 0, dp(5))
        })
        content.addView(text(
            context,
            "${patient.age} years · ${patient.sex} · ${patient.patientNumber}",
            14f,
            false
        ))
        content.addView(text(context, patient.location, 14f, false).apply {
            setPadding(0, dp(4), 0, dp(22))
        })

        section(context, content, "Health information")
        infoRow(context, content, "Blood group", patient.bloodGroup)
        infoRow(context, content, "Allergies", patient.allergies)
        infoRow(context, content, "Active conditions", patient.conditions.joinToString(", "))

        section(context, content, "Medical history")
        patient.visits.forEach { visit ->
            val visitBlock = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(14), dp(12), dp(14), dp(12))
                setBackgroundColor(ContextCompat.getColor(context, R.color.card_surface))
            }
            visitBlock.addView(text(context, visit.date, 14f, true, R.color.uhc_green_dark))
            visitBlock.addView(text(context, visit.facility, 14f, true).apply {
                setPadding(0, dp(5), 0, dp(3))
            })
            visitBlock.addView(text(context, visit.summary, 13f, false))
            content.addView(visitBlock, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(10)
            })
        }

        content.addView(text(context, getString(R.string.demo_data_notice), 12f, false).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(12), 0, 0)
        })
        scrollView.addView(content)
        return scrollView
    }

    private fun section(context: android.content.Context, parent: LinearLayout, title: String) {
        parent.addView(text(context, title, 17f, true).apply {
            setPadding(0, dp(12), 0, dp(8))
        })
    }

    private fun infoRow(context: android.content.Context, parent: LinearLayout, label: String, value: String) {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(10), dp(14), dp(10))
            setBackgroundColor(ContextCompat.getColor(context, R.color.card_surface))
        }
        row.addView(text(context, label, 12f, false, R.color.text_secondary))
        row.addView(text(context, value, 14f, true).apply {
            setPadding(0, dp(3), 0, 0)
        })
        parent.addView(row, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dp(2)
        })
    }

    private fun text(
        context: android.content.Context,
        value: String,
        size: Float,
        bold: Boolean,
        color: Int = R.color.text_primary
    ) = TextView(context).apply {
        text = value
        textSize = size
        if (bold) typeface = Typeface.DEFAULT_BOLD
        setTextColor(ContextCompat.getColor(context, color))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val ARG_PATIENT_ID = "patient_id"

        fun newInstance(patientId: String?) = PatientDetailFragment().apply {
            arguments = Bundle().apply {
                if (patientId != null) putString(ARG_PATIENT_ID, patientId)
            }
        }
    }
}
