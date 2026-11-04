package com.example.coursework.pager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public final class PagerPageFragment extends Fragment {
    public PagerPageFragment() { super(); }
    static PagerPageFragment newInstance(String title, String body) {
        PagerPageFragment fragment = new PagerPageFragment();
        Bundle args = new Bundle(); args.putString("title", title); args.putString("body", body);
        fragment.setArguments(args); return fragment;
    }
    @Nullable @Override public View onCreateView(android.view.LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle state) {
        TextView page = new TextView(requireContext());
        Bundle args = requireArguments();
        page.setText(args.getString("title") + "\n\n" + args.getString("body"));
        page.setTextSize(22); page.setGravity(android.view.Gravity.CENTER);
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        page.setPadding(pad, pad, pad, pad); return page;
    }
}
