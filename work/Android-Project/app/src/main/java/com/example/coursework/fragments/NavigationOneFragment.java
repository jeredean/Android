package com.example.coursework.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursework.R;
import com.google.android.material.snackbar.Snackbar;

/** Task 5: content reached through the Material app bar's options menu. */
public final class NavigationOneFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.action_primary).setOnClickListener(
                ignored -> Snackbar.make(view, R.string.action_completed, Snackbar.LENGTH_SHORT).show());
    }
}
