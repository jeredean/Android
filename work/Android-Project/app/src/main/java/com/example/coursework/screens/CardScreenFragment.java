package com.example.coursework.screens;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.textview.MaterialTextView;

/** Shared, accessible presentation for the sample coursework destinations. */
public abstract class CardScreenFragment extends Fragment {
    protected abstract String heading();
    protected abstract String[] entries();

    @Nullable @Override public android.view.View onCreateView(android.view.LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scroll = new ScrollView(requireContext());
        scroll.setFillViewport(true);
        LinearLayout column = new LinearLayout(requireContext());
        column.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        column.setPadding(pad, pad, pad, dp(110));

        if (heading() != null && !heading().trim().isEmpty()) {
            MaterialTextView title = new MaterialTextView(requireContext());
            title.setText(heading());
            title.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_HeadlineSmall);
            title.setTextColor(MaterialColors.getColor(title, com.google.android.material.R.attr.colorOnSurface));
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(-1, -2);
            titleParams.bottomMargin = dp(8);
            column.addView(title, titleParams);
        }

        for (String entry : entries()) {
            MaterialCardView card = new MaterialCardView(requireContext());
            card.setRadius(dp(16));
            card.setStrokeWidth(dp(1));
            card.setStrokeColor(MaterialColors.getColor(card, com.google.android.material.R.attr.colorOutlineVariant));
            card.setCardElevation(dp(0));
            card.setCardBackgroundColor(MaterialColors.getColor(card,
                    com.google.android.material.R.attr.colorSurfaceContainerLow));

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(-1, -2);
            cardParams.topMargin = dp(14);

            LinearLayout cardContent = new LinearLayout(requireContext());
            cardContent.setOrientation(LinearLayout.VERTICAL);
            int cardPad = dp(20);
            cardContent.setPadding(cardPad, cardPad, cardPad, cardPad);

            String[] parts = entry.split("\n", 2);
            MaterialTextView cardTitle = new MaterialTextView(requireContext());
            cardTitle.setText(parts[0]);
            cardTitle.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
            cardTitle.setTextColor(MaterialColors.getColor(cardTitle, com.google.android.material.R.attr.colorOnSurface));
            cardContent.addView(cardTitle);

            if (parts.length > 1) {
                MaterialTextView cardDesc = new MaterialTextView(requireContext());
                cardDesc.setText(parts[1]);
                cardDesc.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium);
                cardDesc.setTextColor(MaterialColors.getColor(cardDesc, com.google.android.material.R.attr.colorOnSurfaceVariant));
                LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(-1, -2);
                descParams.topMargin = dp(6);
                cardContent.addView(cardDesc, descParams);
            }

            card.addView(cardContent, new MaterialCardView.LayoutParams(-1, -2));
            column.addView(card, cardParams);
        }
        scroll.addView(column);
        return scroll;
    }

    protected int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
