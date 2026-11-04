package com.example.coursework.patients;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.coursework.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.textview.MaterialTextView;

/** Demo UHC patient list with phone detail navigation and tablet two-pane behavior. */
public final class PatientListFragment extends Fragment {
    private static final String[][] PATIENTS = {
        {"P-1001", "Amina N. · 34 years", "Outpatient follow-up · 12 Sep"},
        {"P-1002", "Kato M. · 52 years", "Hypertension review · 10 Sep"},
        {"P-1003", "Grace A. · 27 years", "Antenatal visit · 09 Sep"},
        {"P-1004", "John O. · 61 years", "Diabetes review · 06 Sep"},
        {"P-1005", "Sarah K. · 19 years", "Immunization visit · 04 Sep"}
    };
    private PatientSelectionViewModel selection;
    private boolean tabletLayout;

    public PatientListFragment() { super(); }

    @Nullable @Override public View onCreateView(android.view.LayoutInflater inflater,
            @Nullable android.view.ViewGroup container, @Nullable Bundle state) {
        selection = new ViewModelProvider(requireActivity()).get(PatientSelectionViewModel.class);
        boolean tablet = getResources().getConfiguration().smallestScreenWidthDp >= 600;
        tabletLayout = tablet;
        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(tablet ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(110));

        LinearLayout list = new LinearLayout(requireContext());
        list.setOrientation(LinearLayout.VERTICAL);

        MaterialTextView heading = new MaterialTextView(requireContext());
        heading.setText("UHC · Patient summaries");
        heading.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_HeadlineSmall);
        heading.setTextColor(MaterialColors.getColor(heading, com.google.android.material.R.attr.colorOnSurface));
        list.addView(heading);

        MaterialTextView subtitle = new MaterialTextView(requireContext());
        subtitle.setText("Universal Health Coverage · Active patient records");
        subtitle.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
        subtitle.setTextColor(MaterialColors.getColor(subtitle, com.google.android.material.R.attr.colorOnSurfaceVariant));
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(-1, -2);
        subParams.topMargin = dp(2);
        subParams.bottomMargin = dp(6);
        list.addView(subtitle, subParams);

        for (String[] patient : PATIENTS) list.addView(patientCard(patient));

        if (tablet) {
            root.addView(list, new LinearLayout.LayoutParams(0, -1, 0.42f));
            android.widget.FrameLayout detail = new android.widget.FrameLayout(requireContext());
            detail.setId(R.id.patient_detail_pane);
            LinearLayout.LayoutParams paneParams = new LinearLayout.LayoutParams(0, -1, 0.58f);
            paneParams.leftMargin = dp(16);
            root.addView(detail, paneParams);
        } else {
            root.addView(list, new LinearLayout.LayoutParams(-1, 0, 1f));
        }
        return root;
    }

    @Override public void onViewCreated(@androidx.annotation.NonNull View view,
                                        @Nullable Bundle state) {
        super.onViewCreated(view, state);
        if (tabletLayout && state == null) showTabletDetail(PATIENTS[0][0]);
    }

    private View patientCard(String[] patient) {
        MaterialCardView card = new MaterialCardView(requireContext());
        card.setRadius(dp(16));
        card.setStrokeWidth(dp(1));
        card.setStrokeColor(MaterialColors.getColor(card, com.google.android.material.R.attr.colorOutlineVariant));
        card.setCardElevation(dp(0));
        card.setCardBackgroundColor(MaterialColors.getColor(card,
                com.google.android.material.R.attr.colorSurfaceContainerLow));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(10);
        card.setLayoutParams(params);

        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(16), dp(16), dp(16), dp(16));

        // Avatar circle with initials
        MaterialCardView avatar = new MaterialCardView(requireContext());
        avatar.setRadius(dp(22));
        avatar.setCardElevation(dp(0));
        avatar.setStrokeWidth(0);
        avatar.setCardBackgroundColor(MaterialColors.getColor(avatar,
                com.google.android.material.R.attr.colorPrimaryContainer));
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dp(44), dp(44));
        avatarParams.rightMargin = dp(14);

        MaterialTextView initials = new MaterialTextView(requireContext());
        initials.setGravity(Gravity.CENTER);
        initials.setText(getInitials(patient[1]));
        initials.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
        initials.setTextColor(MaterialColors.getColor(initials,
                com.google.android.material.R.attr.colorOnPrimaryContainer));
        avatar.addView(initials, new MaterialCardView.LayoutParams(-1, -1));

        // Text details column
        LinearLayout content = new LinearLayout(requireContext());
        content.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(0, -2, 1f);

        MaterialTextView name = new MaterialTextView(requireContext());
        name.setText(patient[1]);
        name.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
        name.setTextColor(MaterialColors.getColor(name, com.google.android.material.R.attr.colorOnSurface));

        MaterialTextView summary = new MaterialTextView(requireContext());
        summary.setText(patient[2]);
        summary.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
        summary.setTextColor(MaterialColors.getColor(summary, com.google.android.material.R.attr.colorOnSurfaceVariant));
        LinearLayout.LayoutParams summaryParams = new LinearLayout.LayoutParams(-1, -2);
        summaryParams.topMargin = dp(2);

        content.addView(name);
        content.addView(summary, summaryParams);

        row.addView(avatar, avatarParams);
        row.addView(content, contentParams);

        card.addView(row);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(view -> {
            selection.select(patient[0]);
            if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                showTabletDetail(patient[0]);
            } else {
                Bundle args = new Bundle();
                args.putString("patientId", patient[0]);
                Navigation.findNavController(view).navigate(R.id.patient_detail, args);
            }
        });
        return card;
    }

    private String getInitials(String nameText) {
        String[] parts = nameText.split(" ");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return nameText.substring(0, Math.min(2, nameText.length())).toUpperCase();
    }

    private void showTabletDetail(String patientId) {
        Bundle args = new Bundle();
        args.putString("patientId", patientId);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.patient_detail_pane, PatientDetailFragment.newInstance(patientId))
                .commit();
    }

    private int dp(int n) { return (int) (n * getResources().getDisplayMetrics().density + 0.5f); }
}
