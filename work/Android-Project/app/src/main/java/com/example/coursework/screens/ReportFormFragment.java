package com.example.coursework.screens;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

/** Local demo report form. A real deployment should submit through an authenticated service. */
public final class ReportFormFragment extends Fragment {
    public ReportFormFragment() { super(); }
    @Nullable @Override public View onCreateView(android.view.LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle state) {
        ScrollView scroll = new ScrollView(requireContext());
        scroll.setFillViewport(true);

        LinearLayout page = new LinearLayout(requireContext());
        page.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        page.setPadding(pad, pad, pad, dp(120));

        MaterialTextView title = new MaterialTextView(requireContext());
        title.setText("Submit Service Issue Report");
        title.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_HeadlineSmall);
        title.setTextColor(MaterialColors.getColor(title, com.google.android.material.R.attr.colorOnSurface));
        page.addView(title);

        MaterialTextView subtitle = new MaterialTextView(requireContext());
        subtitle.setText("Enter report details for district infrastructure or service issues");
        subtitle.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
        subtitle.setTextColor(MaterialColors.getColor(subtitle, com.google.android.material.R.attr.colorOnSurfaceVariant));
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(-1, -2);
        subParams.topMargin = dp(4);
        subParams.bottomMargin = dp(16);
        page.addView(subtitle, subParams);

        MaterialCardView card = new MaterialCardView(requireContext());
        card.setRadius(dp(16));
        card.setStrokeWidth(dp(1));
        card.setStrokeColor(MaterialColors.getColor(card, com.google.android.material.R.attr.colorOutlineVariant));
        card.setCardElevation(dp(0));
        card.setCardBackgroundColor(MaterialColors.getColor(card,
                com.google.android.material.R.attr.colorSurfaceContainerLow));

        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(pad, pad, pad, pad);

        TextInputLayout districtLayout = new TextInputLayout(requireContext(), null,
                com.google.android.material.R.attr.textInputOutlinedStyle);
        districtLayout.setHint("District or service area");
        TextInputEditText district = new TextInputEditText(districtLayout.getContext());
        district.setSingleLine(true);
        districtLayout.addView(district);

        TextInputLayout issueLayout = new TextInputLayout(requireContext(), null,
                com.google.android.material.R.attr.textInputOutlinedStyle);
        issueLayout.setHint("Describe the service issue");
        TextInputEditText issue = new TextInputEditText(issueLayout.getContext());
        issue.setMinLines(3);
        issue.setMaxLines(5);
        issueLayout.addView(issue);

        MaterialButton submit = new MaterialButton(requireContext());
        submit.setText("Save report draft");
        LinearLayout.LayoutParams submitParams = new LinearLayout.LayoutParams(-1, -2);
        submitParams.topMargin = dp(20);

        LinearLayout.LayoutParams spacing = new LinearLayout.LayoutParams(-1, -2);
        spacing.topMargin = dp(14);

        form.addView(districtLayout);
        form.addView(issueLayout, spacing);
        form.addView(submit, submitParams);

        card.addView(form);
        page.addView(card);

        submit.setOnClickListener(v -> {
            if (district.getText() == null || district.getText().toString().trim().isEmpty()) {
                districtLayout.setError("Enter a district or service area"); return;
            }
            if (issue.getText() == null || issue.getText().toString().trim().isEmpty()) {
                issueLayout.setError("Describe the issue"); return;
            }
            districtLayout.setError(null); issueLayout.setError(null);
            Snackbar.make(page, "Draft saved on this screen. Connect a service to submit it.",
                    Snackbar.LENGTH_LONG).show();
        });

        scroll.addView(page);
        return scroll;
    }
    private int dp(int n) { return (int) (n * getResources().getDisplayMetrics().density + 0.5f); }
}
