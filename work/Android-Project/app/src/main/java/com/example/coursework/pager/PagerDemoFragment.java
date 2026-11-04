package com.example.coursework.pager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/** Runnable Task 7 sample using a FragmentStateAdapter and Material tab indicators. */
public final class PagerDemoFragment extends Fragment {
    private static final String[] TITLES = {"Welcome", "Services", "Offline"};
    public PagerDemoFragment() { super(); }

    @Nullable @Override public View onCreateView(android.view.LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle state) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        TabLayout tabs = new TabLayout(requireContext());
        ViewPager2 pager = new ViewPager2(requireContext());
        pager.setId(View.generateViewId());
        layout.addView(tabs, new LinearLayout.LayoutParams(-1, -2));
        layout.addView(pager, new LinearLayout.LayoutParams(-1, 0, 1));
        pager.setAdapter(new PageAdapter(this));
        new TabLayoutMediator(tabs, pager, (tab, position) -> tab.setText(TITLES[position])).attach();
        return layout;
    }

    private static final class PageAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
        PageAdapter(Fragment host) { super(host); }
        @Override public int getItemCount() { return TITLES.length; }
        @Override public Fragment createFragment(int position) {
            return PagerPageFragment.newInstance(TITLES[position],
                    "Swipe between pages. Each page is an independently managed Fragment.");
        }
    }
}
