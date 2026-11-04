package com.example.uhc

import android.content.Context
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

class PatientListFragment : Fragment() {

    interface OnPatientSelectedListener {
        fun onPatientSelected(patient: Patient)
    }

    private var listener: OnPatientSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnPatientSelectedListener
            ?: error("The host activity must implement OnPatientSelectedListener")
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val scrollView = ScrollView(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.surface))
            isFillViewport = true
        }
        val content = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(24))
        }
        content.addView(text(context, getString(R.string.patient_list_title), 28f, true, R.color.text_primary))
        content.addView(text(context, getString(R.string.patient_list_subtitle), 14f, false, R.color.text_secondary).apply {
            setPadding(0, dp(6), 0, dp(20))
        })
        content.addView(text(context, getString(R.string.patient_list_count), 16f, true, R.color.text_primary).apply {
            setPadding(0, 0, 0, dp(10))
        })

        PatientRepository.patients.forEach { patient ->
            content.addView(patientCard(context, patient), LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(10)
            })
        }

        content.addView(text(context, getString(R.string.demo_data_notice), 12f, false, R.color.text_secondary).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(14), 0, 0)
        })
        scrollView.addView(content)
        return scrollView
    }

    private fun patientCard(context: Context, patient: Patient): View {
        val card = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            setBackgroundColor(ContextCompat.getColor(context, R.color.card_surface))
            isClickable = true
            isFocusable = true
            setOnClickListener { listener?.onPatientSelected(patient) }
            contentDescription = "${patient.name}, ${patient.age}, ${patient.location}"
        }
        val initials = TextView(context).apply {
            text = patient.name.split(" ").take(2).mapNotNull { it.firstOrNull() }.joinToString("")
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.uhc_green_dark))
            setBackgroundColor(ContextCompat.getColor(context, R.color.status_background))
        }
        card.addView(initials, LinearLayout.LayoutParams(dp(46), dp(46)).apply {
            marginEnd = dp(14)
        })
        val details = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        details.addView(text(context, patient.name, 16f, true, R.color.text_primary))
        details.addView(text(
            context,
            "${patient.age} years · ${patient.sex} · ${patient.patientNumber}",
            12f,
            false,
            R.color.text_secondary
        ).apply {
            setPadding(0, dp(4), 0, 0)
        })
        details.addView(text(context, patient.location, 12f, false, R.color.text_secondary).apply {
            setPadding(0, dp(3), 0, 0)
        })
        card.addView(details, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        card.addView(text(context, "›", 27f, false, R.color.text_secondary))
        return card
    }

    private fun text(
        context: Context,
        value: String,
        size: Float,
        bold: Boolean,
        color: Int
    ) = TextView(context).apply {
        text = value
        textSize = size
        if (bold) typeface = Typeface.DEFAULT_BOLD
        setTextColor(ContextCompat.getColor(context, color))
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
