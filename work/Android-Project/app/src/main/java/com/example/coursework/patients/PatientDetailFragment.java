package com.example.coursework.patients;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.textview.MaterialTextView;

/** Displays a synthetic patient summary; sample records contain no real patient data. */
public final class PatientDetailFragment extends Fragment {
    public PatientDetailFragment() { super(); }
    public static PatientDetailFragment newInstance(String id) {
        PatientDetailFragment fragment = new PatientDetailFragment();
        Bundle args = new Bundle(); args.putString("patientId", id); fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override public View onCreateView(android.view.LayoutInflater inflater,
            @Nullable android.view.ViewGroup container, @Nullable Bundle state) {
        String id = getArguments() == null ? null : getArguments().getString("patientId");
        if (id == null && getActivity() != null) {
            id = new ViewModelProvider(requireActivity()).get(PatientSelectionViewModel.class)
                    .getSelectedPatientId().getValue();
        }

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setFillViewport(true);

        LinearLayout column = new LinearLayout(requireContext());
        column.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        column.setPadding(pad, pad, pad, dp(110));

        MaterialTextView title = new MaterialTextView(requireContext());
        title.setText("Patient summary · " + (id == null ? "Select a patient" : id));
        title.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_HeadlineSmall);
        title.setTextColor(MaterialColors.getColor(title, com.google.android.material.R.attr.colorOnSurface));
        column.addView(title);

        MaterialCardView card = new MaterialCardView(requireContext());
        card.setRadius(dp(16));
        card.setStrokeWidth(dp(1));
        card.setStrokeColor(MaterialColors.getColor(card, com.google.android.material.R.attr.colorOutlineVariant));
        card.setCardElevation(dp(0));
        card.setCardBackgroundColor(MaterialColors.getColor(card,
                com.google.android.material.R.attr.colorSurfaceContainerLow));
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(-1, -2);
        cardParams.topMargin = dp(16);

        LinearLayout cardContent = new LinearLayout(requireContext());
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(pad, pad, pad, pad);

        String[] details = {
            "Recent visit: Follow-up consultation",
            "Medication: See clinician's treatment plan",
            "Allergy status: Confirm with patient on arrival"
        };

        for (String detail : details) {
            MaterialTextView item = new MaterialTextView(requireContext());
            item.setText(detail);
            item.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
            item.setTextColor(MaterialColors.getColor(item, com.google.android.material.R.attr.colorOnSurface));
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(-1, -2);
            itemParams.bottomMargin = dp(10);
            cardContent.addView(item, itemParams);
        }

        MaterialTextView note = new MaterialTextView(requireContext());
        note.setText("This is synthetic training data. Do not enter real patient information.");
        note.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodySmall);
        note.setTextColor(MaterialColors.getColor(note, com.google.android.material.R.attr.colorOnSurfaceVariant));
        LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(-1, -2);
        noteParams.topMargin = dp(6);
        cardContent.addView(note, noteParams);

        card.addView(cardContent);
        column.addView(card, cardParams);

        scroll.addView(column);
        return scroll;
    }
    private int dp(int n) { return (int) (n * getResources().getDisplayMetrics().density + 0.5f); }
}
